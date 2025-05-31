package leoric.pizzacipollastorage.DTOs.Inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientResponseDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InventorySnapshotResponseDto {
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH")
    private LocalDateTime timestamp;

    private float measuredQuantity;
    private String note;

    private IngredientResponseDto ingredient;
}