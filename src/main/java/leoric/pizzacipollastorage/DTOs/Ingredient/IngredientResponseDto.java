package leoric.pizzacipollastorage.DTOs.Ingredient;

import leoric.pizzacipollastorage.vat.dtos.ProductCategory.ProductCategoryResponseDto;

import java.util.UUID;

public record IngredientResponseDto(
        UUID id,
        String name,
        String unit,
        float lossCleaningFactor,
        float lossUsageFactor,
        ProductCategoryResponseDto productCategory,
        Float preferredFullStockLevel,
        Float warningStockLevel,
        Float minimumStockLevel
) {
}