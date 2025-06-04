package leoric.pizzacipollastorage.DTOs.PurchaseInvoice;

import lombok.Data;

@Data
public class PurchaseInvoiceItemCreateDto {
    private Long ingredientId;
    private float quantity;
    private float unitPriceWithoutTax;
    private Long vatRateId;
}