package leoric.pizzacipollastorage.DTOs.MenuItemSale;

import jakarta.validation.constraints.NotNull;
import leoric.pizzacipollastorage.models.enums.DishSize;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MenuItemSaleCreateDto {
    private UUID menuItemId;
    private DishSize dishSize;
    private Float quantitySold;
    private String cookName;
    private String thirdPartyName;
    @NotNull(message = "lastSaleTimestamp is mandatory attribute")
    private LocalDateTime lastSaleTimestamp;
}