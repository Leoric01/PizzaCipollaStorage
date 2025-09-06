package leoric.pizzacipollastorage.DTOs.MenuItem;

import leoric.pizzacipollastorage.models.enums.DishSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemResponseDto {
    private UUID id;
    private String name;
    private String description;
    private DishSize dishSize;
    private MenuItemCategoryResponseDto menuItemCategory;
    private List<RecipeIngredientVeryShortDtoWithId> ingredients;
}