package leoric.pizzacipollastorage.inventory.dtos.Inventory;

import leoric.pizzacipollastorage.models.enums.IngredientState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventorySnapshotCreateDto {
    private UUID ingredientId;
    private Float measuredQuantity;
    private String note;
    private IngredientState form;
}