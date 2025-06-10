package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.MenuItem.*;
import leoric.pizzacipollastorage.models.MenuItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface MenuItemService {

    MenuItem createMenuItem(MenuItemCreateDto dto);

    RecipeIngredientShortDto addIngredientToMenuItem(RecipeIngredientCreateDto dto);

    List<MenuItemResponseDto> getAllMenuItems();

    List<RecipeIngredientShortDto> addIngredientsToMenuItemBulk(RecipeCreateBulkDto dto);

    MenuItemResponseDto getMenuItemById(UUID id);
}