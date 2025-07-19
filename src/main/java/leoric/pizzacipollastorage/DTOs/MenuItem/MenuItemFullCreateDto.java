package leoric.pizzacipollastorage.DTOs.MenuItem;

import leoric.pizzacipollastorage.models.enums.DishSize;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class MenuItemFullCreateDto {
    private String name;
    private String description;
    private DishSize size;
    private UUID menuItemCategoryId;

    private MenuItemCategoryCreateDto menuItemCategory;

    private List<RecipeIngredientSimpleDto> ingredients;

    @Data
    public static class RecipeIngredientSimpleDto {
        private UUID ingredientId;
        private Float quantity;
    }
}