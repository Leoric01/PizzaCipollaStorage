package leoric.pizzacipollastorage.DTOs.MenuItem;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class MenuItemWithIngredientsCreateDto {
    private String name;
    private String description;
    private UUID dishSizeId; // optional

    private List<RecipeIngredientSimpleDto> ingredients; // optional

    @Data
    public static class RecipeIngredientSimpleDto {
        private String ingredientName;
        private Float quantity;
    }
}