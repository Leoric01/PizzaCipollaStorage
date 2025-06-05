package leoric.pizzacipollastorage.DTOs.PurchaseInvoice;

import lombok.Data;

@Data
public class PurchaseInvoiceItemCreateDto {
    private String ingredientName;
    private float quantity;
    private float unitPriceWithoutTax;
}