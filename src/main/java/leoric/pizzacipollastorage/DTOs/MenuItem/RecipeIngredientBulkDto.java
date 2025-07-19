package leoric.pizzacipollastorage.DTOs.MenuItem;

import lombok.Data;

@Data
public class RecipeIngredientBulkDto {
    private String ingredientName; // nebo alias
    private Float quantity; // povinné pokud dishSizeId == 1, jinak volitelné
}