package leoric.pizzacipollastorage.DTOs.Ingredient;

import leoric.pizzacipollastorage.models.enums.ProductCategory;
import lombok.Data;

@Data
public class IngredientCreateDto {
    private String name;
    private String unit;
    private float lossCleaningFactor;
    private float lossUsageFactor;
    private ProductCategory category;
}