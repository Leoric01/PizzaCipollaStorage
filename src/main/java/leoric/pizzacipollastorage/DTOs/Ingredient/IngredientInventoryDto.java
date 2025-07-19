package leoric.pizzacipollastorage.DTOs.Ingredient;

import leoric.pizzacipollastorage.vat.dtos.VatRateShortDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientInventoryDto {
    private UUID id;
    private String name;
    private String unit;

    private float measuredQuantity;

    private VatRateShortDto vatRate;
}