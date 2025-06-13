package leoric.pizzacipollastorage.DTOs.MenuItem;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class MenuItemResponseDto {
    private UUID id;
    private String name;
    private String description;
    private MenuItemCategoryResponseDto menuItemCategory;
    private List<RecipeIngredientVeryShortDtoWithId> ingredients;
}