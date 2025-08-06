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
@RequestMapping("/api/menu-item-category/{branchId}")
@RequiredArgsConstructor
public class MenuItemCategoryController {
    private final MenuItemCategoryService menuItemCategoryService;

    @GetMapping
    public ResponseEntity<List<MenuItemCategoryResponseDto>> menuItemCategoryGetAll(
            @PathVariable UUID branchId) {
        return ResponseEntity.ok(menuItemCategoryService.findAll(branchId));
    }

    @PostMapping
    public ResponseEntity<MenuItemCategoryResponseDto> menuItemCreate(
            @PathVariable UUID branchId,
            @RequestBody MenuItemCategoryCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(menuItemCategoryService.add(branchId, dto));
    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<MenuItemCategoryResponseDto> updateCategory(
//            @PathVariable UUID branchId,
//            @PathVariable UUID id,
//            @RequestBody MenuItemCategoryCreateDto dto) {
//        return ResponseEntity.ok(menuItemCategoryService.update(branchId, id, dto));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCategory(
//            @PathVariable UUID branchId,
//            @PathVariable UUID id) {
//        menuItemCategoryService.delete(branchId, id);
//        return ResponseEntity.noContent().build();
//    }
}