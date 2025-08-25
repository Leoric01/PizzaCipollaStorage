package leoric.pizzacipollastorage.DTOs.Ingredient;

import java.util.UUID;

public record IngredientShortDto(
        UUID id,
        String name,
        String unit
) {
}