package leoric.pizzacipollastorage.DTOs.Ingredient;

import lombok.Data;

@Data
public class IngredientCreateDto {
    private String name;
    private String unit;
    private float lossCleaningFactor;
    private float lossUsageFactor;
    private String category;
}