package leoric.pizzacipollastorage.DTOs.MenuItem;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RecipeCreateBulkDto {
    private String menuItem;
    private UUID dishSizeId; // volitelné, default = 1
    private List<RecipeIngredientBulkDto> ingredients;
}