package leoric.pizzacipollastorage.DTOs.MenuItem;

import leoric.pizzacipollastorage.models.enums.DishSize;

import java.util.List;
import java.util.UUID;

public record MenuItemDuplicateDifferentDishSizesRequestDto(
        UUID menuItemId,
        List<DishSize> dishSizes
) {
}