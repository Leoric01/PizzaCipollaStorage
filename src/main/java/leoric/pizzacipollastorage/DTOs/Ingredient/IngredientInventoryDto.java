package leoric.pizzacipollastorage.DTOs.Ingredient;

import leoric.pizzacipollastorage.DTOs.Vat.VatRateShortDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientInventoryDto {
    private Long id;
    private String name;
    private String unit;

    private float measuredQuantity;

    private VatRateShortDto vatRate;
}