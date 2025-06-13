package leoric.pizzacipollastorage.inventory.dtos.Inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import leoric.pizzacipollastorage.models.enums.IngredientState;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class InventorySnapshotCreateDto {
    private UUID ingredientId;
    private Float measuredQuantity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH")
    private LocalDateTime timestamp;
    private String note;

    private IngredientState form;
}