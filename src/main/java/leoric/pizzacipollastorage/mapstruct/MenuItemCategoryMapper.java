package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemCategoryCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemCategoryResponseDto;
import leoric.pizzacipollastorage.models.MenuItemCategory;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MenuItemCategoryMapper {

    MenuItemCategoryResponseDto toDto(MenuItemCategory entity);

    List<MenuItemCategoryResponseDto> toDtoList(List<MenuItemCategory> entities);

    MenuItemCategory toEntity(MenuItemCategoryCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget MenuItemCategory entity, MenuItemCategoryCreateDto dto);
}