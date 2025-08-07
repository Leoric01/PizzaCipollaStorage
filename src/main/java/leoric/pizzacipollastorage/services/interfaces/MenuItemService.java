package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemFullCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface MenuItemService {

    MenuItemResponseDto createMenuItemWithOptionalIngredients(UUID branchId, MenuItemFullCreateDto dto);

    List<MenuItemResponseDto> createMenuItemsBulk(UUID branchId, List<MenuItemFullCreateDto> dtos);

    List<MenuItemResponseDto> menuItemGetAll(UUID branchId);

    MenuItemResponseDto menuItemGetById(UUID branchId, UUID menuItemId);

    MenuItemResponseDto menuItemUpdate(UUID branchId, UUID menuItemId, MenuItemFullCreateDto dto);

    MenuItemResponseDto menuItemGetByName(UUID branchId, String menuItemName);

    void menuItemDeleteById(UUID branchId, UUID menuItemId);

    // -------- RecipeIngredient methods --------

//    RecipeIngredientShortDto addIngredientToMenuItem(UUID branchId, RecipeIngredientCreateDto dto);
//
//    List<RecipeIngredientShortDto> addIngredientsToMenuItemBulk(UUID branchId, RecipeCreateBulkDto dto);
//
//    RecipeIngredientShortDto updateRecipeIngredient(UUID branchId, UUID id, RecipeIngredientVeryShortDto dto);
//
//    RecipeIngredientShortDto getRecipeIngredientById(UUID branchId, UUID id);
//
//    void deleteRecipeIngredientById(UUID branchId, UUID id);
}