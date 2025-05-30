package leoric.pizzacipollastorage.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InventorySnapshotCreateDto {
    private Long ingredientId;
    private float measuredQuantity;
    private LocalDateTime timestamp;
    private String note;
}