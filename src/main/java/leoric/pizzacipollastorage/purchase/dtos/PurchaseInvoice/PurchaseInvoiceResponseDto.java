package leoric.pizzacipollastorage.purchase.dtos.PurchaseInvoice;

import leoric.pizzacipollastorage.purchase.dtos.Supplier.SupplierShortDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class PurchaseInvoiceResponseDto {
    private UUID id;
    private String invoiceNumber;
    private SupplierShortDto supplier;
    private LocalDate issuedDate;
    private LocalDate receivedDate;
    private String note;
    private List<PurchaseInvoiceItemDto> items;
}