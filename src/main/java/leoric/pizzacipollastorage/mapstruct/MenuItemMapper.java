package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemResponseDto;
import leoric.pizzacipollastorage.DTOs.MenuItem.RecipeIngredientVeryShortDtoWithId;
import leoric.pizzacipollastorage.models.MenuItem;
import leoric.pizzacipollastorage.models.RecipeIngredient;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {IngredientMapper.class})
public interface MenuItemMapper {

    @Mapping(source = "recipeIngredients", target = "ingredients")
    @Mapping(source = "category", target = "menuItemCategory")
    MenuItemResponseDto toDto(MenuItem menuItem);

    List<MenuItemResponseDto> toDtoList(List<MenuItem> menuItems);

    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "ingredient", target = "ingredient")
    RecipeIngredientVeryShortDtoWithId toVeryShortDto(RecipeIngredient recipeIngredient);

    MenuItem toEntity(MenuItemCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget MenuItem entity, MenuItemCreateDto dto);
}