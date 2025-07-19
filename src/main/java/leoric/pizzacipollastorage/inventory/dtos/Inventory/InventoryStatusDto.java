package leoric.pizzacipollastorage.inventory.dtos.Inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH")
    private LocalDateTime timestamp;
}