package leoric.pizzacipollastorage.DTOs.Ingredient;

import leoric.pizzacipollastorage.DTOs.ProductCategory.ProductCategoryResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientResponseDto {
    private UUID id;
    private String name;
    private String unit;
    private float lossCleaningFactor;
    private float lossUsageFactor;
    private ProductCategoryResponseDto productCategory;

    private Float preferredFullStockLevel;
    private Float warningStockLevel;
    private Float minimumStockLevel;
}