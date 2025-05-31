package leoric.pizzacipollastorage.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientShortDto {
    private Long id;
    private IngredientShortDto ingredient;
    private float amount;
}