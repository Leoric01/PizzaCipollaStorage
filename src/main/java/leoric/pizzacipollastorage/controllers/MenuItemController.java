package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemFullCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemResponseDto;
import leoric.pizzacipollastorage.services.interfaces.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/menu-items/{branchId}")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @PostMapping
    public ResponseEntity<MenuItemResponseDto> createMenuItem(@PathVariable UUID branchId, @RequestBody MenuItemFullCreateDto dto) {
        return ResponseEntity.ok(menuItemService.createMenuItemWithOptionalIngredients(branchId, dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponseDto> updateMenuItem(
            @PathVariable UUID branchId,
            @PathVariable UUID id,
            @RequestBody MenuItemFullCreateDto dto) {
        MenuItemResponseDto updated = menuItemService.updateMenuItem(branchId, id, dto);
        return ResponseEntity.ok(updated);
    }

//    @GetMapping
//    public ResponseEntity<List<MenuItemResponseDto>> getAllMenuItems(@PathVariable UUID branchId) {
//        return ResponseEntity.ok(menuItemService.getAllMenuItems());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<MenuItemResponseDto> getMenuItemById(@PathVariable UUID branchId, @PathVariable UUID id) {
//        return ResponseEntity.ok(menuItemService.getMenuItemById(id));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteMenuItemById(@PathVariable UUID branchId, @PathVariable UUID id) {
//        menuItemService.deleteMenuItemById(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PatchMapping("/recipes/{id}")
//    public ResponseEntity<RecipeIngredientShortDto> updateRecipeIngredientById(
//            @PathVariable UUID branchId,
//            @PathVariable UUID id,
//            @RequestBody RecipeIngredientVeryShortDto dto) {
//        return ResponseEntity.ok(menuItemService.updateRecipeIngredient(id, dto));
//    }
//
//    @GetMapping("/recipes/{id}")
//    public ResponseEntity<RecipeIngredientShortDto> getRecipeIngredientById(@PathVariable UUID branchId, @PathVariable UUID id) {
//        return ResponseEntity.ok(menuItemService.getRecipeIngredientById(id));
//    }
//
//    @DeleteMapping("/recipes/{id}")
//    public ResponseEntity<Void> deleteRecipeIngredientById(@PathVariable UUID branchId, @PathVariable UUID id) {
//        menuItemService.deleteRecipeIngredientById(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping("/recipes")
//    public ResponseEntity<RecipeIngredientShortDto> addIngredientToMenuItem(@PathVariable UUID branchId, @RequestBody RecipeIngredientCreateDto dto) {
//        return ResponseEntity.ok(menuItemService.addIngredientToMenuItem(branchId, dto));
//    }
//
//    @PostMapping("/recipes/bulk")
//    public ResponseEntity<List<RecipeIngredientShortDto>> addIngredientsToMenuItemBulk(@PathVariable UUID branchId, @RequestBody RecipeCreateBulkDto dto) {
//        return ResponseEntity.ok(menuItemService.addIngredientsToMenuItemBulk(branchId, dto));
//    }
//
//    @GetMapping("/name/{name}")
//    public ResponseEntity<MenuItemResponseDto> getMenuItemByNormalizedName(@PathVariable UUID branchId, @PathVariable String name) {
//        MenuItemResponseDto response = menuItemService.getMenuItemByName(branchId, name);
//        return ResponseEntity.ok(response);
//    }

}