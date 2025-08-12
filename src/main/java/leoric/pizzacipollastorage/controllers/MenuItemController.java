package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.MenuItem.*;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchServiceAccess;
import leoric.pizzacipollastorage.services.interfaces.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Page<MenuItemResponseDto>> menuItemGetAll(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) String search,
            Pageable pageable // automaticky bere ?page=0&size=10&sort=name,asc
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(menuItemService.menuItemGetAll(branchId, search, pageable));
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
// TODO nahazaet do postamana a protestovat
    @PostMapping("/recipes")
    public ResponseEntity<RecipeIngredientShortDto> recipeIngredientAdd(
            @PathVariable UUID branchId,
            @RequestBody RecipeIngredientCreateDto recipeIngredientCreateDto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(menuItemService.recipeIngredientAddToMenuItem(branchId, recipeIngredientCreateDto));
    }

    @PostMapping("/recipes/bulk")
    public ResponseEntity<List<RecipeIngredientShortDto>> recipeIngredientAddBulk(
            @PathVariable UUID branchId,
            @RequestBody RecipeCreateBulkDto recipeCreateBulkDto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(menuItemService.recipeIngredientAddToMenuItemBulk(branchId, recipeCreateBulkDto));
    }

    @PatchMapping("/recipes/{recipeIngredientId}")
    public ResponseEntity<RecipeIngredientShortDto> recipeIngredientUpdate(
            @PathVariable UUID branchId,
            @PathVariable UUID recipeIngredientId,
            @RequestBody RecipeIngredientVeryShortDto recipeIngredientVeryShortDto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(menuItemService.updateRecipeIngredient(branchId, recipeIngredientId, recipeIngredientVeryShortDto));
    }

    @GetMapping("/recipes/{recipeIngredientId}")
    public ResponseEntity<RecipeIngredientShortDto> recipeIngredientFindById(
            @PathVariable UUID branchId,
            @PathVariable UUID recipeIngredientId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(menuItemService.getRecipeIngredientById(branchId, recipeIngredientId));
    }

    @DeleteMapping("/recipes/{recipeIngredientId}")
    public ResponseEntity<Void> recipeIngredientDelete(
            @PathVariable UUID branchId,
            @PathVariable UUID recipeIngredientId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        menuItemService.deleteRecipeIngredientById(branchId, recipeIngredientId);
        return ResponseEntity.noContent().build();
    }
}