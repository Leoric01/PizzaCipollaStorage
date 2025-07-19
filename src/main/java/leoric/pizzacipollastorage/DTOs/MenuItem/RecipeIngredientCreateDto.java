package leoric.pizzacipollastorage.DTOs.MenuItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientCreateDto {
    private UUID menuItemId;
    private UUID ingredientId;
    private Float quantity;
}