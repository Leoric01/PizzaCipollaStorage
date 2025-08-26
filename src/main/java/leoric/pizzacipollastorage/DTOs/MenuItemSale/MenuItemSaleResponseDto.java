package leoric.pizzacipollastorage.DTOs.MenuItemSale;

import leoric.pizzacipollastorage.models.enums.DishSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemSaleResponseDto {
    private UUID id;
    private String menuItem;
    private DishSize dishSize;
    private Float quantitySold;
    private String cookName;
    private LocalDateTime saleDate;
}