package leoric.pizzacipollastorage.purchase.dtos.PurchaseInvoice;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientShortDto;
import leoric.pizzacipollastorage.vat.dtos.VatRateShortDto;
import lombok.Data;

import java.util.UUID;

@Data
public class PurchaseInvoiceItemDto {
    private UUID id;
    private IngredientShortDto ingredient; // název a id
    private float quantity;
    private float unitPriceWithoutTax;
    private VatRateShortDto vatRate;
}