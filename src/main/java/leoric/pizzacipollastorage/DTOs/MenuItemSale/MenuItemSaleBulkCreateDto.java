package leoric.pizzacipollastorage.DTOs.MenuItemSale;

import jakarta.validation.constraints.NotNull;
import leoric.pizzacipollastorage.models.enums.DishSize;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class MenuItemSaleBulkCreateDto {

    @NotNull(message = "lastSaleTimestamp is mandatory attribute")
    private LocalDateTime lastSaleTimestamp;

    private String cookName;

    @NotNull(message = "items list cannot be null")
    private List<MenuItemSaleBulkItemDto> items;

    @Data
    public static class MenuItemSaleBulkItemDto {
        private UUID menuItemId;
        private DishSize dishSize;
        private Float quantitySold;
        private String thirdPartyName;
    }
}