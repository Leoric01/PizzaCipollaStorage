package leoric.pizzacipollastorage.DTOs.Pizza;

import lombok.Data;

@Data
public class BulkRecipeIngredientDto {
    private String ingredientName; // nebo alias
    private Float quantity; // povinné pokud dishSizeId == 1, jinak volitelné
}