package leoric.pizzacipollastorage.DTOs.MenuItem;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientShortDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientVeryShortDtoWithId {
    private UUID id;
    private IngredientShortDto ingredient;
    private float quantity;
}