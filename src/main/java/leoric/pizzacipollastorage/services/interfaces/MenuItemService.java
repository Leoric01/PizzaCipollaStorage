package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.MenuItem.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface MenuItemService {

    MenuItemResponseDto createMenuItem(MenuItemCreateDto dto);

    RecipeIngredientShortDto addIngredientToMenuItem(RecipeIngredientCreateDto dto);

    List<MenuItemResponseDto> getAllMenuItems();

    List<RecipeIngredientShortDto> addIngredientsToMenuItemBulk(RecipeCreateBulkDto dto);

    MenuItemResponseDto getMenuItemById(UUID id);

    MenuItemResponseDto createMenuItemWithOptionalIngredients(MenuItemWithIngredientsCreateDto dto);

    MenuItemResponseDto getMenuItemByName(String menuItemName);

    void deleteMenuItemById(UUID id);

    MenuItemResponseDto updateMenuItem(UUID id, MenuItemWithIngredientsCreateDto dto);

    RecipeIngredientShortDto updateRecipeIngredient(UUID id, RecipeIngredientVeryShortDto dto);

    RecipeIngredientShortDto getRecipeIngredientById(UUID id);

    void deleteRecipeIngredientById(UUID id);
}