package leoric.pizzacipollastorage.DTOs.PurchaseInvoice;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseInvoiceCreateDto {
    private String invoiceNumber;
    private Long supplierId;
    private LocalDateTime issuedDate;
    private LocalDateTime receivedDate;
    private String note;
    private List<PurchaseInvoiceItemCreateDto> items;
}