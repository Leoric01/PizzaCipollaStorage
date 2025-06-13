package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.MenuItem.*;
import leoric.pizzacipollastorage.services.interfaces.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @PatchMapping("/recipes/{id}")
    public ResponseEntity<RecipeIngredientShortDto> updateRecipeIngredientById(
            @PathVariable UUID id,
            @RequestBody RecipeIngredientVeryShortDto dto) {
        return ResponseEntity.ok(menuItemService.updateRecipeIngredient(id, dto));
    }

    @GetMapping("/recipes/{id}")
    public ResponseEntity<RecipeIngredientShortDto> getRecipeIngredientById(@PathVariable UUID id) {
        return ResponseEntity.ok(menuItemService.getRecipeIngredientById(id));
    }

    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<Void> deleteRecipeIngredientById(@PathVariable UUID id) {
        menuItemService.deleteRecipeIngredientById(id);
        return ResponseEntity.noContent().build();
    }
//
//    @PostMapping("/without-ingredients")
//    public ResponseEntity<MenuItemResponseDto> createMenuItemWithoutIngredients(@RequestBody MenuItemCreateDto dto) {
//        return ResponseEntity.ok(menuItemService.createMenuItem(dto));
//    }

    @PostMapping("/recipes")
    public ResponseEntity<RecipeIngredientShortDto> addIngredientToMenuItem(@RequestBody RecipeIngredientCreateDto dto) {
        return ResponseEntity.ok(menuItemService.addIngredientToMenuItem(dto));
    }

    @PostMapping("/recipes/bulk")
    public ResponseEntity<List<RecipeIngredientShortDto>> addIngredientsToMenuItemBulk(@RequestBody RecipeCreateBulkDto dto) {
        return ResponseEntity.ok(menuItemService.addIngredientsToMenuItemBulk(dto));
    }

    @PostMapping
    public ResponseEntity<MenuItemResponseDto> createMenuItemWith(@RequestBody MenuItemWithIngredientsCreateDto dto) {
        return ResponseEntity.ok(menuItemService.createMenuItemWithOptionalIngredients(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItemById(@PathVariable UUID id) {
        menuItemService.deleteMenuItemById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponseDto> updateMenuItem(
            @PathVariable UUID id,
            @RequestBody MenuItemWithIngredientsCreateDto dto) {
        MenuItemResponseDto updated = menuItemService.updateMenuItem(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<MenuItemResponseDto>> getAllMenuItems() {
        return ResponseEntity.ok(menuItemService.getAllMenuItems());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<MenuItemResponseDto> getMenuItemByNormalizedName(@PathVariable String name) {
        MenuItemResponseDto response = menuItemService.getMenuItemByName(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponseDto> getMenuItemById(@PathVariable UUID id) {
        return ResponseEntity.ok(menuItemService.getMenuItemById(id));
    }
}