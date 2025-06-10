package leoric.pizzacipollastorage.DTOs.Ingredient;

import lombok.Data;

import java.util.UUID;

@Data
public class IngredientCreateDto {
    private String name;
    private String unit;
    private float lossCleaningFactor;
    private float lossUsageFactor;
    private UUID vatRateId;
}