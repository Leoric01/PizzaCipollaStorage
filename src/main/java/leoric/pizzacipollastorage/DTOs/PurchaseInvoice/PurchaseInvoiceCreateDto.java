package leoric.pizzacipollastorage.DTOs.PurchaseInvoice;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PurchaseInvoiceCreateDto {
    private String invoiceNumber;
    private String supplierName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate issuedDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate receivedDate;
    private String note;
    private List<PurchaseInvoiceItemCreateDto> items;
}