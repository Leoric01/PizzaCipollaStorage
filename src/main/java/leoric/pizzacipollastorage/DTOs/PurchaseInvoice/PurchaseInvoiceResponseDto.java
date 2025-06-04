package leoric.pizzacipollastorage.DTOs.PurchaseInvoice;

import leoric.pizzacipollastorage.DTOs.Supplier.SupplierShortDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseInvoiceResponseDto {
    private Long id;
    private String invoiceNumber;
    private SupplierShortDto supplier; // pouze jméno a id
    private LocalDateTime issuedDate;
    private LocalDateTime receivedDate;
    private String note;
    private List<PurchaseInvoiceItemDto> items;
}