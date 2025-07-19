package leoric.pizzacipollastorage.DTOs.MenuItemSale;

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
    private String dishSize;
    private int quantitySold;
    private String cookName;
    private LocalDateTime saleDate;
}