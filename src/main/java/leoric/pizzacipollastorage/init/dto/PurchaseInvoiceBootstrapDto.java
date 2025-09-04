package leoric.pizzacipollastorage.init.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseInvoiceBootstrapDto {
    private String invoiceNumber;
    private String supplierName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate issuedDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate receivedDate;
    private String note;
    private List<PurchaseInvoiceItemBootstrapDto> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PurchaseInvoiceItemBootstrapDto {
        private String ingredientName;
        private float quantity;
        private float unitPriceWithoutTax;
    }
}