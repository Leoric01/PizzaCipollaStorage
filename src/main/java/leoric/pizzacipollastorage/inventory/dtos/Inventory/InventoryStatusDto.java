package leoric.pizzacipollastorage.inventory.dtos.Inventory;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryStatusDto {
    private IngredientResponseDto ingredient;
    private float measuredQuantity;
    private LocalDateTime timestamp;
}