package leoric.pizzacipollastorage.DTOs.Inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import leoric.pizzacipollastorage.models.enums.IngredientForm;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InventorySnapshotCreateDto {
    private Long ingredientId;
    private Float measuredQuantity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH")
    private LocalDateTime timestamp;
    private String note;

    private IngredientForm form;
}