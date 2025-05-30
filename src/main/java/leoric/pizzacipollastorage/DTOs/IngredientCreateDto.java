package leoric.pizzacipollastorage.DTOs;

import lombok.Data;

@Data
public class IngredientCreateDto {
    private String name;
    private String unit;
    private float lossCleaningFactor;
    private float lossUsageFactor;
    private Long vatRateId;
}