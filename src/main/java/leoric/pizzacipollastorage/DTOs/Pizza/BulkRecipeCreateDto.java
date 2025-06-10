package leoric.pizzacipollastorage.DTOs.Pizza;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class BulkRecipeCreateDto {
    private String pizzaName;
    private UUID dishSizeId; // volitelné, default = 1
    private List<BulkRecipeIngredientDto> ingredients;
}