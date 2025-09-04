package leoric.pizzacipollastorage.init.dto;

import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemCategoryCreateDto;
import leoric.pizzacipollastorage.models.enums.DishSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemBootstrapDto {
    private String name;
    private String description;
    private DishSize size;
    private MenuItemCategoryCreateDto menuItemCategory;
    private List<RecipeIngredientBootstrapDto> ingredients;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecipeIngredientBootstrapDto {
        private String ingredientName;
        private Float quantity;
    }
}