package leoric.pizzacipollastorage.DTOs.MenuItemSale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuItemNameWithSizesDto {
    private String name;
    private List<MenuItemSizeDto> dishSizes;
}