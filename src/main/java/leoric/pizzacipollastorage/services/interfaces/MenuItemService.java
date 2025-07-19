package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.MenuItem.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface MenuItemService {

    MenuItemResponseDto createMenuItem(MenuItemCreateDto dto);

    RecipeIngredientShortDto addIngredientToMenuItem(UUID branchId, RecipeIngredientCreateDto dto);

    List<MenuItemResponseDto> getAllMenuItems();

    List<RecipeIngredientShortDto> addIngredientsToMenuItemBulk(UUID branchId, RecipeCreateBulkDto dto);

    MenuItemResponseDto getMenuItemById(UUID id);

    MenuItemResponseDto createMenuItemWithOptionalIngredients(UUID branchId, MenuItemFullCreateDto dto);

    MenuItemResponseDto getMenuItemByName(UUID branchId, String menuItemName);

    void deleteMenuItemById(UUID id);

    MenuItemResponseDto updateMenuItem(UUID branchId, UUID id, MenuItemFullCreateDto dto);

    RecipeIngredientShortDto updateRecipeIngredient(UUID id, RecipeIngredientVeryShortDto dto);

    RecipeIngredientShortDto getRecipeIngredientById(UUID id);

    void deleteRecipeIngredientById(UUID id);
}