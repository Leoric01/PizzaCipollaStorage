package leoric.pizzacipollastorage.DTOs.MenuItem;

import leoric.pizzacipollastorage.models.enums.MenuItemCategory;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class MenuItemResponseDto {
    private UUID id;
    private String name;
    private String description;
    private MenuItemCategory category;
    private List<RecipeIngredientVeryShortDtoWithId> ingredients;
}