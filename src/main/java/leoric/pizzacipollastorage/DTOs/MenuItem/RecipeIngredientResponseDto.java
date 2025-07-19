package leoric.pizzacipollastorage.DTOs.MenuItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientResponseDto {
    private UUID id;
    private UUID ingredientId;
    private UUID dishSizeId;
    private Float quantity;
    private Boolean autoScaleWithDishSize;
}