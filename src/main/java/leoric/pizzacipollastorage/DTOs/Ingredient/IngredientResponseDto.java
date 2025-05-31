package leoric.pizzacipollastorage.DTOs.Ingredient;

import leoric.pizzacipollastorage.DTOs.Vat.VatRateShortDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientResponseDto {
    private Long id;
    private String name;
    private String unit;
    private float lossCleaningFactor;
    private float lossUsageFactor;
    private VatRateShortDto vatRate;
}