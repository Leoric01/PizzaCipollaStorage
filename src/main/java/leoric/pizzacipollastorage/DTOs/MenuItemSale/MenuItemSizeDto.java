package leoric.pizzacipollastorage.DTOs.MenuItemSale;

import leoric.pizzacipollastorage.models.enums.DishSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuItemSizeDto {
    private UUID menuItemId;
    private DishSize size;  // enum přímo
}