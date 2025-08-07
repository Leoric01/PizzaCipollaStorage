package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemFullCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemResponseDto;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchServiceAccess;
import leoric.pizzacipollastorage.services.interfaces.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/menu-items/{branchId}")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;
    private final BranchServiceAccess branchServiceAccess;

    @PostMapping
    public ResponseEntity<MenuItemResponseDto> menuItemCreate(
            @PathVariable UUID branchId,
            @RequestBody MenuItemFullCreateDto menuItemFullCreateDto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(menuItemService.createMenuItemWithOptionalIngredients(branchId, menuItemFullCreateDto));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<MenuItemResponseDto>> menuItemCreateBulk(
            @PathVariable UUID branchId,
            @RequestBody List<MenuItemFullCreateDto> dtos,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(menuItemService.createMenuItemsBulk(branchId, dtos));
    }

    @GetMapping
    public ResponseEntity<List<MenuItemResponseDto>> menuItemGetAll(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(menuItemService.menuItemGetAll(branchId));
    }

    @GetMapping("/{menuItemId}")
    public ResponseEntity<MenuItemResponseDto> menuItemGetById(
            @PathVariable UUID branchId,
            @PathVariable UUID menuItemId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(menuItemService.menuItemGetById(branchId, menuItemId));
    }

    @PutMapping("/{menuItemId}")
    public ResponseEntity<MenuItemResponseDto> menuItemUpdate(
            @PathVariable UUID branchId,
            @PathVariable UUID menuItemId,
            @RequestBody MenuItemFullCreateDto menuItemFullCreateDto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(menuItemService.menuItemUpdate(branchId, menuItemId, menuItemFullCreateDto));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<MenuItemResponseDto> menuItemGetByName(
            @PathVariable UUID branchId,
            @PathVariable String name,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(menuItemService.menuItemGetByName(branchId, name));
    }

    @DeleteMapping("/{menuItemId}")
    public ResponseEntity<Void> menuItemDeleteById(
            @PathVariable UUID branchId,
            @PathVariable UUID menuItemId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        menuItemService.menuItemDeleteById(branchId, menuItemId);
        return ResponseEntity.noContent().build();
    }

    // ---------- RecipeIngredient endpoints ----------

//    @PostMapping("/recipes")
//    public ResponseEntity<RecipeIngredientShortDto> recipeIngredientAdd(
//            @PathVariable UUID branchId,
//            @RequestBody RecipeIngredientCreateDto dto,
//            @AuthenticationPrincipal User currentUser
//    ) {
//        branchServiceAccess.assertHasAccess(branchId, currentUser);
//        return ResponseEntity.ok(menuItemService.addIngredientToMenuItem(branchId, dto));
//    }
//
//    @PostMapping("/recipes/bulk")
//    public ResponseEntity<List<RecipeIngredientShortDto>> recipeIngredientAddBulk(
//            @PathVariable UUID branchId,
//            @RequestBody RecipeCreateBulkDto dto,
//            @AuthenticationPrincipal User currentUser
//    ) {
//        branchServiceAccess.assertHasAccess(branchId, currentUser);
//        return ResponseEntity.ok(menuItemService.addIngredientsToMenuItemBulk(branchId, dto));
//    }
//
//    @PatchMapping("/recipes/{id}")
//    public ResponseEntity<RecipeIngredientShortDto> recipeIngredientUpdate(
//            @PathVariable UUID branchId,
//            @PathVariable UUID id,
//            @RequestBody RecipeIngredientVeryShortDto dto,
//            @AuthenticationPrincipal User currentUser
//    ) {
//        branchServiceAccess.assertHasAccess(branchId, currentUser);
//        return ResponseEntity.ok(menuItemService.updateRecipeIngredient(branchId, id, dto));
//    }
//
//    @GetMapping("/recipes/{id}")
//    public ResponseEntity<RecipeIngredientShortDto> recipeIngredientFindById(
//            @PathVariable UUID branchId,
//            @PathVariable UUID id,
//            @AuthenticationPrincipal User currentUser
//    ) {
//        branchServiceAccess.assertHasAccess(branchId, currentUser);
//        return ResponseEntity.ok(menuItemService.getRecipeIngredientById(branchId, id));
//    }
//
//    @DeleteMapping("/recipes/{id}")
//    public ResponseEntity<Void> recipeIngredientDelete(
//            @PathVariable UUID branchId,
//            @PathVariable UUID id,
//            @AuthenticationPrincipal User currentUser
//    ) {
//        branchServiceAccess.assertHasAccess(branchId, currentUser);
//        menuItemService.deleteRecipeIngredientById(branchId, id);
//        return ResponseEntity.noContent().build();
//    }
}