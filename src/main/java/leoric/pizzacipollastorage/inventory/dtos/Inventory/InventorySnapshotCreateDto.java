package leoric.pizzacipollastorage.inventory.dtos.Inventory;

import leoric.pizzacipollastorage.models.enums.IngredientState;
import lombok.Data;

import java.util.UUID;

@Data
public class InventorySnapshotCreateDto {
    private UUID ingredientId;
    private Float measuredQuantity;
    private String note;
    private IngredientState form;
}