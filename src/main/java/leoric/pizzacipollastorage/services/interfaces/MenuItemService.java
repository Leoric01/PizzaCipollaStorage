package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.MenuItem.*;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemMapNameResponseDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemNameWithSizesDto;
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

    Page<MenuItemMapNameResponseDto> menuItemGetAllMapNames(UUID branchId, String search, Pageable pageable);

    List<MenuItemNameWithSizesDto> getMenuItemNamesWithSizes(UUID branchId);

    void addThirdPartyName(UUID menuItemId, UUID branchId, String name);

    void updateThirdPartyName(UUID menuItemId, UUID branchId, String oldName, String newName);

    void deleteThirdPartyName(UUID menuItemId, UUID branchId, String name);

    List<MenuItemResponseDto> getMenuItemsByThirdPartyName(UUID branchId, String thirdPartyName);

    List<MenuItemResponseDto> duplicateMenuItemsDifferentDishSizes(UUID branchId, MenuItemDuplicateDifferentDishSizesRequestDto requestDto);
}