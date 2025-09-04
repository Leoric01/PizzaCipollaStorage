package leoric.pizzacipollastorage.init.dto;

import leoric.pizzacipollastorage.models.enums.IngredientState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventorySnapshotBootstrapDto {
    private String ingredientName;
    private Float measuredQuantity;
    private String note;
    private IngredientState form;
}