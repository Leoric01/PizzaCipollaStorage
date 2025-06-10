package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientShortDto;
import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemResponseDto;
import leoric.pizzacipollastorage.DTOs.MenuItem.RecipeIngredientShortDto;
import leoric.pizzacipollastorage.DTOs.MenuItem.RecipeIngredientVeryShortDto;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.MenuItem;
import leoric.pizzacipollastorage.models.RecipeIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {IngredientMapper.class})
public interface MenuItemMapper {

    MenuItemResponseDto toDto(MenuItem menuItem);

    List<MenuItemResponseDto> toDtoList(List<MenuItem> menuItems);

    @Mapping(source = "quantity", target = "amount")
    @Mapping(source = "ingredient", target = "ingredient")
    RecipeIngredientShortDto toShortDto(RecipeIngredient recipeIngredient);
    @Mapping(source = "quantity", target = "amount")
    @Mapping(source = "ingredient", target = "ingredient")
    RecipeIngredientVeryShortDto toVeryShortDto(RecipeIngredient recipeIngredient);
    IngredientShortDto toShortDto(Ingredient ingredient);
}