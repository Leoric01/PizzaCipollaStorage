package leoric.pizzacipollastorage.DTOs.MenuItem;

import lombok.Data;

import java.util.UUID;

@Data
public class RecipeIngredientCreateDto {
    private UUID menuItemId;
    private UUID ingredientId;
    private float quantity;
}