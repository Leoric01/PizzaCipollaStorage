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

@Service
@RequiredArgsConstructor
public class PurchaseInvoiceServiceImpl implements PurchaseInvoiceService {

    private final InventoryService inventoryService;

    private final SupplierRepository supplierRepository;
    private final PurchaseInvoiceRepository purchaseInvoiceRepository;
    private final PurchaseInvoiceItemRepository purchaseInvoiceItemRepository;
    private final StockEntryRepository stockEntryRepository;
    private final VatRateRepository vatRateRepository;
    private final IngredientRepository ingredientRepository;

    private final PurchaseInvoiceMapper purchaseInvoiceMapper;

    @Override
    @Transactional
    public PurchaseInvoiceResponseDto createInvoice(PurchaseInvoiceCreateDto dto) {
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

        PurchaseInvoice invoice = PurchaseInvoice.builder()
                .invoiceNumber(dto.getInvoiceNumber())
                .supplier(supplier)
                .issuedDate(dto.getIssuedDate())
                .receivedDate(dto.getReceivedDate())
                .note(dto.getNote())
                .build();

        purchaseInvoiceRepository.save(invoice);
        for (PurchaseInvoiceItemCreateDto itemDto : dto.getItems()) {
            Ingredient ingredient = ingredientRepository.findById(itemDto.getIngredientId())
                    .orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));

            VatRate vatRate = vatRateRepository.findById(itemDto.getVatRateId())
                    .orElseThrow(() -> new EntityNotFoundException("VAT rate not found"));

            PurchaseInvoiceItem item = PurchaseInvoiceItem.builder()
                    .purchaseInvoice(invoice)
                    .ingredient(ingredient)
                    .quantity(itemDto.getQuantity())
                    .unitPriceWithoutTax(itemDto.getUnitPriceWithoutTax())
                    .vatRate(vatRate)
                    .build();

            purchaseInvoiceItemRepository.save(item);

            // stock entry
            StockEntry entry = StockEntry.builder()
                    .ingredient(ingredient)
                    .supplier(supplier)
                    .quantityReceived(itemDto.getQuantity())
                    .pricePerUnitWithoutTax(itemDto.getUnitPriceWithoutTax())
                    .receivedDate(dto.getReceivedDate())
                    .purchaseInvoiceItem(item) // pokud přidáš tuto referenci
                    .build();

            stockEntryRepository.save(entry);

            // inventory snapshot
            inventoryService.addToInventory(ingredient.getId(), itemDto.getQuantity());
        }

        return purchaseInvoiceMapper.toDto(invoice); // případně jiný response
    }

    @Override
    public PurchaseInvoiceResponseDto getById(Long id) {
        PurchaseInvoice invoice = purchaseInvoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Purchase invoice not found"));

        return purchaseInvoiceMapper.toDto(invoice);
    }
}