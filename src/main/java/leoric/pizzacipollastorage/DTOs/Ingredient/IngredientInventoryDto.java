package leoric.pizzacipollastorage.DTOs.Ingredient;

import leoric.pizzacipollastorage.vat.dtos.VatRateShortDto;

import java.util.UUID;

public record IngredientInventoryDto(
        UUID id,
        String name,
        String unit,
        float measuredQuantity,
        VatRateShortDto vatRate
) {
}