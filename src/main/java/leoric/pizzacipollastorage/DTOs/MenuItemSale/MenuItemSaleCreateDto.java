package leoric.pizzacipollastorage.DTOs.MenuItemSale;

import leoric.pizzacipollastorage.models.enums.DishSize;
import lombok.Data;

import java.util.UUID;

@Data
public class MenuItemSaleCreateDto {
    private UUID menuItemId;
    private DishSize dishSize;
    private Float quantitySold;
    private String cookName;
}