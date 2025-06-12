package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemResponseDto;
import leoric.pizzacipollastorage.DTOs.MenuItem.RecipeIngredientVeryShortDtoWithId;
import leoric.pizzacipollastorage.models.MenuItem;
import leoric.pizzacipollastorage.models.RecipeIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {IngredientMapper.class})
public interface MenuItemMapper {

    @Mapping(source = "recipeIngredients", target = "ingredients")
    MenuItemResponseDto toDto(MenuItem menuItem);

    List<MenuItemResponseDto> toDtoList(List<MenuItem> menuItems);

    @Mapping(source = "quantity", target = "amount")
    @Mapping(source = "ingredient", target = "ingredient")
    RecipeIngredientVeryShortDtoWithId toVeryShortDto(RecipeIngredient recipeIngredient);
}