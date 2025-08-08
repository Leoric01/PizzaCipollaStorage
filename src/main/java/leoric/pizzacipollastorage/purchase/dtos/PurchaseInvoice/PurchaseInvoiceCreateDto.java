package leoric.pizzacipollastorage.purchase.dtos.PurchaseInvoice;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PurchaseInvoiceCreateDto {
    private String invoiceNumber;
    private String supplierName;
    private LocalDate issuedDate;
    private LocalDate receivedDate;
    private String note;
    private List<PurchaseInvoiceItemCreateDto> items;
}