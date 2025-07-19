package leoric.pizzacipollastorage.DTOs.Ingredient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientShortDto {
    private UUID id;
    private String name;
    private String unit;
}