package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemCategoryCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemCategoryResponseDto;
import leoric.pizzacipollastorage.services.interfaces.MenuItemCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/menu-item-category")
@RequiredArgsConstructor
public class MenuItemCategoryController {
    private final MenuItemCategoryService menuItemCategoryService;

    @GetMapping
    public ResponseEntity<List<MenuItemCategoryResponseDto>> getAllMenuItems() {
        return ResponseEntity.ok(menuItemCategoryService.findAll());
    }

    @PostMapping
    public ResponseEntity<MenuItemCategoryResponseDto> addCategory(@RequestBody MenuItemCategoryCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItemCategoryService.add(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemCategoryResponseDto> updateCategory(
            @PathVariable UUID id,
            @RequestBody MenuItemCategoryCreateDto dto) {
        return ResponseEntity.ok(menuItemCategoryService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        menuItemCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}