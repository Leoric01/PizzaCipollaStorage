package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import leoric.pizzacipollastorage.DTOs.PurchaseInvoice.PurchaseInvoiceCreateDto;
import leoric.pizzacipollastorage.DTOs.PurchaseInvoice.PurchaseInvoiceItemCreateDto;
import leoric.pizzacipollastorage.DTOs.PurchaseInvoice.PurchaseInvoiceResponseDto;
import leoric.pizzacipollastorage.mapstruct.PurchaseInvoiceMapper;
import leoric.pizzacipollastorage.models.*;
import leoric.pizzacipollastorage.repositories.*;
import leoric.pizzacipollastorage.services.interfaces.InventoryService;
import leoric.pizzacipollastorage.services.interfaces.PurchaseInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseInvoiceServiceImpl implements PurchaseInvoiceService {

    private final InventoryService inventoryService;

    private final SupplierRepository supplierRepository;
    private final PurchaseInvoiceRepository purchaseInvoiceRepository;
    private final PurchaseInvoiceItemRepository purchaseInvoiceItemRepository;
    private final StockEntryRepository stockEntryRepository;
    private final IngredientRepository ingredientRepository;

    private final PurchaseInvoiceMapper purchaseInvoiceMapper;

    @Override
    @Transactional
    public PurchaseInvoiceResponseDto createInvoice(PurchaseInvoiceCreateDto dto) {
        Supplier supplier = supplierRepository.findByNameIgnoreCase(dto.getSupplierName())
                .orElseThrow(() -> new EntityNotFoundException("Dodavatel '" + dto.getSupplierName() + "' nebyl nalezen."));

        PurchaseInvoice invoice = PurchaseInvoice.builder()
                .invoiceNumber(dto.getInvoiceNumber())
                .supplier(supplier)
                .issuedDate(dto.getIssuedDate())
                .receivedDate(dto.getReceivedDate())
                .note(dto.getNote())
                .build();

        purchaseInvoiceRepository.save(invoice);

        List<PurchaseInvoiceItem> savedItems = new ArrayList<>();

        for (PurchaseInvoiceItemCreateDto itemDto : dto.getItems()) {
            Ingredient ingredient = ingredientRepository.findByNameIgnoreCase(itemDto.getIngredientName())
                    .orElseThrow(() -> new EntityNotFoundException("Ingredience '" + itemDto.getIngredientName() + "' nebyla nalezena."));

            VatRate vatRate = ingredient.getCategory().getVatRate();

            PurchaseInvoiceItem item = PurchaseInvoiceItem.builder()
                    .purchaseInvoice(invoice)
                    .ingredient(ingredient)
                    .quantity(itemDto.getQuantity())
                    .unitPriceWithoutTax(itemDto.getUnitPriceWithoutTax())
                    .vatRate(vatRate)
                    .build();

            purchaseInvoiceItemRepository.save(item);
            savedItems.add(item);

            StockEntry entry = StockEntry.builder()
                    .ingredient(ingredient)
                    .supplier(supplier)
                    .quantityReceived(itemDto.getQuantity())
                    .pricePerUnitWithoutTax(itemDto.getUnitPriceWithoutTax())
                    .receivedDate(dto.getReceivedDate())
                    .purchaseInvoiceItem(item)
                    .build();

            stockEntryRepository.save(entry);
            inventoryService.addToInventory(ingredient.getId(), itemDto.getQuantity());
        }
        invoice.setItems(savedItems);

        return purchaseInvoiceMapper.toDto(invoice);
    }

    @Override
    public PurchaseInvoiceResponseDto getById(UUID id) {
        PurchaseInvoice invoice = purchaseInvoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Purchase invoice not found"));

        return purchaseInvoiceMapper.toDto(invoice);
    }

    @Override
    public List<PurchaseInvoiceResponseDto> getLatestInvoices(int limit) {
        List<PurchaseInvoice> invoices = purchaseInvoiceRepository
                .findTop10ByOrderByIssuedDateDesc();

        return invoices.stream()
                .map(purchaseInvoiceMapper::toDto)
                .toList();
    }
}