package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.MenuItem.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface MenuItemService {
    Page<MenuItemResponseDto> menuItemGetAll(UUID branchId, String search, Pageable pageable);

    MenuItemResponseDto createMenuItemWithOptionalIngredients(UUID branchId, MenuItemFullCreateDto dto);

    List<MenuItemResponseDto> createMenuItemsBulk(UUID branchId, List<MenuItemFullCreateDto> dtos);


    MenuItemResponseDto menuItemGetById(UUID branchId, UUID menuItemId);

    MenuItemResponseDto menuItemUpdate(UUID branchId, UUID menuItemId, MenuItemFullCreateDto dto);

    MenuItemResponseDto menuItemGetByName(UUID branchId, String menuItemName);

    void menuItemDeleteById(UUID branchId, UUID menuItemId);

    // -------- RecipeIngredient methods --------

    RecipeIngredientShortDto recipeIngredientAddToMenuItem(UUID branchId, RecipeIngredientCreateDto dto);

    List<RecipeIngredientShortDto> recipeIngredientAddToMenuItemBulk(UUID branchId, RecipeCreateBulkDto dto);

    RecipeIngredientShortDto updateRecipeIngredient(UUID branchId, UUID recipeIngredientId, RecipeIngredientVeryShortDto dto);

    RecipeIngredientShortDto getRecipeIngredientById(UUID branchId, UUID recipeIngredientId);

    void deleteRecipeIngredientById(UUID branchId, UUID id);
}