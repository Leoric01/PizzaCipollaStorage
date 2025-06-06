package leoric.pizzacipollastorage.DTOs.Pizza;

import lombok.Data;

import java.util.List;

@Data
public class BulkRecipeCreateDto {
    private String pizzaName;
    private Integer dishSizeId; // volitelné, default = 1
    private List<BulkRecipeIngredientDto> ingredients;
}