package leoric.pizzacipollastorage.DTOs.Pizza;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientShortDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientVeryShortDto {
    private IngredientShortDto ingredient;
    private float amount;
}