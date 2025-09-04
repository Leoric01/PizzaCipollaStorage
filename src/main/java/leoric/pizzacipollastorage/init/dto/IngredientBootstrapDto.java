package leoric.pizzacipollastorage.init.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientBootstrapDto {
    private String name;
    private String unit;
    private float lossCleaningFactor;
    private float lossUsageFactor;
    private String productCategoryName;
    private Float preferredFullStockLevel;
    private Float warningStockLevel;
    private Float minimumStockLevel;
}