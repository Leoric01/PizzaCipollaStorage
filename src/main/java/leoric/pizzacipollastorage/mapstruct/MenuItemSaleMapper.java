package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleResponseDto;
import leoric.pizzacipollastorage.models.MenuItem;
import leoric.pizzacipollastorage.models.MenuItemSale;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MenuItemSaleMapper {

    @Mapping(source = "menuItem.name", target = "menuItemName")
    MenuItemSaleResponseDto toDto(MenuItemSale entity);

    List<MenuItemSaleResponseDto> toDtoList(List<MenuItemSale> entities);

    MenuItemSale toEntity(MenuItemSaleResponseDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget MenuItemSale entity, MenuItemSaleResponseDto dto);

    String mapMenuItemToString(MenuItem menuItem);

    MenuItem mapStringToMenuItem(String menuItemName);
}