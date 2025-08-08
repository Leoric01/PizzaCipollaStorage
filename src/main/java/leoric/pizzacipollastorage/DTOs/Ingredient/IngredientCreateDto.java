package leoric.pizzacipollastorage.DTOs.Ingredient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record IngredientCreateDto(
        @NotBlank(message = "Název ingredience (name) nesmí být prázdný")
        String name,
        @NotBlank(message = "unit nesmí být prázdná")
        String unit,
        float lossCleaningFactor,
        float lossUsageFactor,
        @NotNull(message = "Chybí productCategoryId")
        UUID productCategoryId,
        Float preferredFullStockLevel,
        Float warningStockLevel,
        Float minimumStockLevel
) {
}