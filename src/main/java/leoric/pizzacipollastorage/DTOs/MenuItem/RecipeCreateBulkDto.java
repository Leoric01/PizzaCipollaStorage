package leoric.pizzacipollastorage.DTOs.MenuItem;

import lombok.Data;

import java.util.List;

@Data
public class RecipeCreateBulkDto {
    private String menuItem;
    private List<RecipeIngredientBulkDto> ingredients;
}