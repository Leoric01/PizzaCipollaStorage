package leoric.pizzacipollastorage.models.enums;

import lombok.Getter;

import java.util.UUID;

@Getter
public enum ProductCategory {
    FOOD(UUID.fromString("00000000-0000-0000-0000-000000000002")),
    DRINK(UUID.fromString("00000000-0000-0000-0000-000000000001")),
    PACKAGING(UUID.fromString("00000000-0000-0000-0000-000000000001"));

    private final UUID vatRateId;

    ProductCategory(UUID vatRateId) {
        this.vatRateId = vatRateId;
    }
}