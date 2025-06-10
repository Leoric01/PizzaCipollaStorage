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
public class MenuItem {
    private final MenuItemService menuItemService;

    @PostMapping
    public ResponseEntity<leoric.pizzacipollastorage.models.MenuItem> createMenuItem(@RequestBody MenuItemCreateDto dto) {
        return ResponseEntity.ok(menuItemService.createMenuItem(dto));
    }

    @PostMapping("/recipes")
    public ResponseEntity<RecipeIngredientShortDto> addIngredientToMenuItem(@RequestBody RecipeIngredientCreateDto dto) {
        return ResponseEntity.ok(menuItemService.addIngredientToMenuItem(dto));
    }

    @PostMapping("/recipes/bulk")
    public ResponseEntity<List<RecipeIngredientShortDto>> addIngredientsToMenuItemBulk(@RequestBody RecipeCreateBulkDto dto) {
        return ResponseEntity.ok(menuItemService.addIngredientsToMenuItemBulk(dto));
    }

    @GetMapping
    public ResponseEntity<List<MenuItemResponseDto>> getAllMenuItems() {
        return ResponseEntity.ok(menuItemService.getAllMenuItems());
    }
    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponseDto> getMenuItemById(@PathVariable UUID id) {
        return ResponseEntity.ok(menuItemService.getMenuItemById(id));
    }
}