package leoric.pizzacipollastorage.DTOs.MenuItemSale;

import lombok.Data;

import java.util.UUID;

@Data
public class MenuItemSaleCreateDto {
    private UUID menuItemId;
    private UUID dishSizeId;
    private int quantitySold;
    private String cookName;
}