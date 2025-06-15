package leoric.pizzacipollastorage.DTOs.MenuItem;

import leoric.pizzacipollastorage.DTOs.DishSize.DishSizeResponseDto;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class MenuItemResponseDto {
    private UUID id;
    private String name;
    private String description;
    private DishSizeResponseDto dishSize;
    private MenuItemCategoryResponseDto menuItemCategory;
    private List<RecipeIngredientVeryShortDtoWithId> ingredients;
}