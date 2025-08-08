package leoric.pizzacipollastorage.purchase.dtos.PurchaseInvoice;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate issuedDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate receivedDate;
    private String note;
    private List<PurchaseInvoiceItemDto> items;
}