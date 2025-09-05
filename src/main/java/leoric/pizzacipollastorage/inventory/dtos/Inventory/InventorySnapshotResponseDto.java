package leoric.pizzacipollastorage.inventory.dtos.Inventory;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientResponseDto;
import leoric.pizzacipollastorage.models.enums.SnapshotType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class InventorySnapshotResponseDto {
    private UUID id;
    private LocalDateTime timestamp;
    private float measuredQuantity;
    private String note;
    private Float expectedQuantity;
    private Float discrepancy;
    private Float lastDiscrepancy;
    private IngredientResponseDto ingredient;
    private SnapshotType type;
}