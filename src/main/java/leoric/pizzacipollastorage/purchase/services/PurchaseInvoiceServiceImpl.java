package leoric.pizzacipollastorage.purchase.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.inventory.models.InventorySnapshot;
import leoric.pizzacipollastorage.inventory.repositories.InventorySnapshotRepository;
import leoric.pizzacipollastorage.inventory.services.InventoryService;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.StockEntry;
import leoric.pizzacipollastorage.models.enums.IngredientState;
import leoric.pizzacipollastorage.models.enums.SnapshotType;
import leoric.pizzacipollastorage.purchase.PurchaseInvoiceMapper;
import leoric.pizzacipollastorage.purchase.dtos.PurchaseInvoice.PurchaseInvoiceCreateDto;
import leoric.pizzacipollastorage.purchase.dtos.PurchaseInvoice.PurchaseInvoiceItemCreateDto;
import leoric.pizzacipollastorage.purchase.dtos.PurchaseInvoice.PurchaseInvoiceResponseDto;
import leoric.pizzacipollastorage.purchase.models.PurchaseInvoice;
import leoric.pizzacipollastorage.purchase.models.PurchaseInvoiceItem;
import leoric.pizzacipollastorage.purchase.models.Supplier;
import leoric.pizzacipollastorage.purchase.repositories.PurchaseInvoiceItemRepository;
import leoric.pizzacipollastorage.purchase.repositories.PurchaseInvoiceRepository;
import leoric.pizzacipollastorage.purchase.repositories.SupplierRepository;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.repositories.StockEntryRepository;
import leoric.pizzacipollastorage.vat.models.VatRate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseInvoiceServiceImpl implements PurchaseInvoiceService {

    private final InventoryService inventoryService;
    private final InventorySnapshotRepository inventorySnapshotRepository;
    private final SupplierRepository supplierRepository;
    private final PurchaseInvoiceRepository purchaseInvoiceRepository;
    private final PurchaseInvoiceItemRepository purchaseInvoiceItemRepository;
    private final StockEntryRepository stockEntryRepository;
    private final IngredientRepository ingredientRepository;
    private final BranchRepository branchRepository;
    private final PurchaseInvoiceMapper purchaseInvoiceMapper;

    @Override
    @Transactional
    public PurchaseInvoiceResponseDto createInvoice(UUID branchId, PurchaseInvoiceCreateDto dto) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        Supplier supplier = supplierRepository.findByNameIgnoreCaseAndBranchId(dto.getSupplierName(), branchId)
                .orElseThrow(() -> new EntityNotFoundException("Dodavatel '" + dto.getSupplierName() + "' nebyl nalezen."));

        PurchaseInvoice invoice = PurchaseInvoice.builder()
                .invoiceNumber(dto.getInvoiceNumber())
                .supplier(supplier)
                .issuedDate(dto.getIssuedDate())
                .receivedDate(dto.getReceivedDate())
                .note(dto.getNote())
                .branch(branch)
                .build();

        purchaseInvoiceRepository.save(invoice);

        List<PurchaseInvoiceItem> savedItems = new ArrayList<>();
        for (PurchaseInvoiceItemCreateDto itemDto : dto.getItems()) {
            Ingredient ingredient = ingredientRepository.findByNameIgnoreCaseAndBranchId(itemDto.getIngredientName(), branchId)
                    .orElseThrow(() -> new EntityNotFoundException("Ingredience '" + itemDto.getIngredientName() + "' nebyla nalezena."));

            VatRate vatRate = ingredient.getProductCategory().getVatRate();

            PurchaseInvoiceItem item = PurchaseInvoiceItem.builder()
                    .purchaseInvoice(invoice)
                    .ingredient(ingredient)
                    .quantity(itemDto.getQuantity())
                    .unitPriceWithoutTax(itemDto.getUnitPriceWithoutTax())
                    .vatRate(vatRate)
                    .build();

            purchaseInvoiceItemRepository.save(item);
            savedItems.add(item);

            Optional<InventorySnapshot> lastSnapshotOpt = inventorySnapshotRepository
                    .findTopByIngredientAndBranchOrderByTimestampDesc(ingredient, branch);

            Float newExpectedQuantity;
            Float newMeasuredQuantity;

            if (lastSnapshotOpt.isPresent()) {
                InventorySnapshot lastSnapshot = lastSnapshotOpt.get();
                newExpectedQuantity = lastSnapshot.getExpectedQuantity() != null
                        ? lastSnapshot.getExpectedQuantity() + itemDto.getQuantity()
                        : itemDto.getQuantity();
                newMeasuredQuantity = lastSnapshot.getMeasuredQuantity() != null
                        ? lastSnapshot.getMeasuredQuantity() + itemDto.getQuantity()
                        : itemDto.getQuantity();
            } else {
                newExpectedQuantity = itemDto.getQuantity();
                newMeasuredQuantity = itemDto.getQuantity();
            }

            InventorySnapshot snapshot = InventorySnapshot.builder()
                    .ingredient(ingredient)
                    .timestamp(LocalDateTime.now())
                    .measuredQuantity(newMeasuredQuantity)
                    .expectedQuantity(newExpectedQuantity)
                    .note("Příjem na sklad z faktury " + dto.getInvoiceNumber())
                    .branch(branch)
                    .type(SnapshotType.STOCK_IN)
                    .form(IngredientState.RAW)
                    .build();

            inventorySnapshotRepository.save(snapshot);
        }

        invoice.setItems(savedItems);
        return purchaseInvoiceMapper.toDto(invoice);
    }

    @Override
    @Transactional
    public PurchaseInvoiceResponseDto update(UUID branchId, UUID invoiceId, PurchaseInvoiceCreateDto dto) {
        PurchaseInvoice invoice = purchaseInvoiceRepository.findByIdAndBranchId(invoiceId, branchId)
                .orElseThrow(() -> new EntityNotFoundException("Purchase invoice not found"));

        Supplier supplier = supplierRepository.findByNameIgnoreCaseAndBranchId(dto.getSupplierName(), branchId)
                .orElseThrow(() -> new EntityNotFoundException("Dodavatel '" + dto.getSupplierName() + "' nebyl nalezen."));

        invoice.setInvoiceNumber(dto.getInvoiceNumber());
        invoice.setSupplier(supplier);
        invoice.setIssuedDate(dto.getIssuedDate());
        invoice.setReceivedDate(dto.getReceivedDate());
        invoice.setNote(dto.getNote());

        // Smazat StockEntry pro staré položky
        for (PurchaseInvoiceItem oldItem : invoice.getItems()) {
            stockEntryRepository.deleteAllByPurchaseInvoiceItemId(oldItem.getId());
        }
        // Smazat staré položky
        purchaseInvoiceItemRepository.deleteAll(invoice.getItems());

        // Uložit nové položky
        List<PurchaseInvoiceItem> updatedItems = new ArrayList<>();
        for (PurchaseInvoiceItemCreateDto itemDto : dto.getItems()) {
            Ingredient ingredient = ingredientRepository.findByNameIgnoreCaseAndBranchId(itemDto.getIngredientName(), branchId)
                    .orElseThrow(() -> new EntityNotFoundException("Ingredience '" + itemDto.getIngredientName() + "' nebyla nalezena."));

            VatRate vatRate = ingredient.getProductCategory().getVatRate();

            PurchaseInvoiceItem item = PurchaseInvoiceItem.builder()
                    .purchaseInvoice(invoice)
                    .ingredient(ingredient)
                    .quantity(itemDto.getQuantity())
                    .unitPriceWithoutTax(itemDto.getUnitPriceWithoutTax())
                    .vatRate(vatRate)
                    .build();

            purchaseInvoiceItemRepository.save(item);
            updatedItems.add(item);
        }

        invoice.setItems(updatedItems);
        return purchaseInvoiceMapper.toDto(invoice);
    }

    @Override
    @Transactional
    public void delete(UUID branchId, UUID invoiceId) {
        PurchaseInvoice invoice = purchaseInvoiceRepository.findByIdAndBranchId(invoiceId, branchId)
                .orElseThrow(() -> new EntityNotFoundException("Purchase invoice not found"));

        // Smazat StockEntry pro všechny položky
        for (PurchaseInvoiceItem item : invoice.getItems()) {
            stockEntryRepository.deleteAllByPurchaseInvoiceItemId(item.getId());
        }
        // Smazat položky a fakturu
        purchaseInvoiceItemRepository.deleteAll(invoice.getItems());
        purchaseInvoiceRepository.delete(invoice);
    }

    @Override
    @Transactional
    public void stockFromInvoice(UUID branchId, UUID invoiceId) {
        PurchaseInvoice invoice = purchaseInvoiceRepository.findByIdAndBranchId(invoiceId, branchId)
                .orElseThrow(() -> new EntityNotFoundException("Purchase invoice not found"));

        for (PurchaseInvoiceItem item : invoice.getItems()) {
            StockEntry entry = StockEntry.builder()
                    .ingredient(item.getIngredient())
                    .supplier(invoice.getSupplier())
                    .quantityReceived(item.getQuantity())
                    .pricePerUnitWithoutTax(item.getUnitPriceWithoutTax())
                    .receivedDate(invoice.getReceivedDate())
                    .purchaseInvoiceItem(item)
                    .build();

            stockEntryRepository.save(entry);
            inventoryService.addToInventory(branchId, item.getIngredient().getId(), item.getQuantity());
        }
    }

    @Override
    public PurchaseInvoiceResponseDto getById(UUID branchId, UUID id) {
        PurchaseInvoice invoice = purchaseInvoiceRepository.findByIdAndBranchId(id, branchId)
                .orElseThrow(() -> new EntityNotFoundException("Purchase invoice not found"));

        return purchaseInvoiceMapper.toDto(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseInvoiceResponseDto> getAll(UUID branchId, Pageable pageable) {
        Page<PurchaseInvoice> page = purchaseInvoiceRepository.findAllByBranchId(branchId, pageable);
        return page.map(purchaseInvoiceMapper::toDto);
    }

    @Override
    public List<PurchaseInvoiceResponseDto> getLatestInvoices(UUID branchId, int limit) {
        return List.of();
    }
}