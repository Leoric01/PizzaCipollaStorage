package leoric.pizzacipollastorage.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import leoric.pizzacipollastorage.DTOs.MenuItem.*;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchServiceAccess;
import leoric.pizzacipollastorage.services.interfaces.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static leoric.pizzacipollastorage.PizzaCipollaStorageApplication.BRANCH_EMPLOYEE;
import static leoric.pizzacipollastorage.PizzaCipollaStorageApplication.BRANCH_MANAGER;

@RestController
@RequestMapping("/api/menu-items/{branchId}")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;
    private final BranchServiceAccess branchServiceAccess;

    // =========================
    // GET endpoints (BRANCH_MANAGER + ADMIN + BRANCH_EMPLOYEE)
    // =========================
    @GetMapping
    public ResponseEntity<Page<MenuItemResponseDto>> menuItemGetAll(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) String search,
            @ParameterObject
            @Parameter(required = false) Pageable pageable
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER + ";" + BRANCH_EMPLOYEE);
        return ResponseEntity.ok(menuItemService.menuItemGetAll(branchId, search, pageable));
    }

    @GetMapping("/{menuItemId}")
    public ResponseEntity<MenuItemResponseDto> menuItemGetById(
            @PathVariable UUID branchId,
            @PathVariable UUID menuItemId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER + ";" + BRANCH_EMPLOYEE);
        return ResponseEntity.ok(menuItemService.menuItemGetById(branchId, menuItemId));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<MenuItemResponseDto> menuItemGetByName(
            @PathVariable UUID branchId,
            @PathVariable String name,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER + ";" + BRANCH_EMPLOYEE);
        return ResponseEntity.ok(menuItemService.menuItemGetByName(branchId, name));
    }

    // =========================
    // POST/PUT/DELETE endpoints (jen BRANCH_MANAGER + ADMIN)
    // =========================
    @PostMapping
    public ResponseEntity<MenuItemResponseDto> menuItemCreate(
            @PathVariable UUID branchId,
            @RequestBody MenuItemFullCreateDto menuItemFullCreateDto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        return ResponseEntity.ok(menuItemService.createMenuItemWithOptionalIngredients(branchId, menuItemFullCreateDto));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<MenuItemResponseDto>> menuItemCreateBulk(
            @PathVariable UUID branchId,
            @RequestBody List<MenuItemFullCreateDto> dtos,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        return ResponseEntity.ok(menuItemService.createMenuItemsBulk(branchId, dtos));
    }

    @PostMapping("/duplicate-bulk")
    public ResponseEntity<List<MenuItemResponseDto>> menuItemDuplicateDifferentDishSizes(
            @PathVariable UUID branchId,
            @RequestBody MenuItemDuplicateDifferentDishSizesRequestDto requestDto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        return ResponseEntity.ok(menuItemService.duplicateMenuItemsDifferentDishSizes(branchId, requestDto));
    }

    @PutMapping("/{menuItemId}")
    public ResponseEntity<MenuItemResponseDto> menuItemUpdate(
            @PathVariable UUID branchId,
            @PathVariable UUID menuItemId,
            @RequestBody MenuItemFullCreateDto menuItemFullCreateDto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        return ResponseEntity.ok(menuItemService.menuItemUpdate(branchId, menuItemId, menuItemFullCreateDto));
    }

    @DeleteMapping("/{menuItemId}")
    public ResponseEntity<Void> menuItemDeleteById(
            @PathVariable UUID branchId,
            @PathVariable UUID menuItemId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        menuItemService.menuItemDeleteById(branchId, menuItemId);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // RecipeIngredient endpoints (jen BRANCH_MANAGER + ADMIN)
    // =========================
    @PostMapping("/recipes")
    public ResponseEntity<RecipeIngredientShortDto> recipeIngredientAdd(
            @PathVariable UUID branchId,
            @RequestBody RecipeIngredientCreateDto recipeIngredientCreateDto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        return ResponseEntity.ok(menuItemService.recipeIngredientAddToMenuItem(branchId, recipeIngredientCreateDto));
    }

    @PostMapping("/recipes/bulk")
    public ResponseEntity<List<RecipeIngredientShortDto>> recipeIngredientAddBulk(
            @PathVariable UUID branchId,
            @RequestBody RecipeCreateBulkDto recipeCreateBulkDto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        return ResponseEntity.ok(menuItemService.recipeIngredientAddToMenuItemBulk(branchId, recipeCreateBulkDto));
    }

    @PatchMapping("/recipes/{recipeIngredientId}")
    public ResponseEntity<RecipeIngredientShortDto> recipeIngredientUpdate(
            @PathVariable UUID branchId,
            @PathVariable UUID recipeIngredientId,
            @RequestBody RecipeIngredientVeryShortDto recipeIngredientVeryShortDto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        return ResponseEntity.ok(menuItemService.updateRecipeIngredient(branchId, recipeIngredientId, recipeIngredientVeryShortDto));
    }

    @GetMapping("/recipes/{recipeIngredientId}")
    public ResponseEntity<RecipeIngredientShortDto> recipeIngredientFindById(
            @PathVariable UUID branchId,
            @PathVariable UUID recipeIngredientId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER + ";" + BRANCH_EMPLOYEE);
        return ResponseEntity.ok(menuItemService.getRecipeIngredientById(branchId, recipeIngredientId));
    }

    @DeleteMapping("/recipes/{recipeIngredientId}")
    public ResponseEntity<Void> recipeIngredientDelete(
            @PathVariable UUID branchId,
            @PathVariable UUID recipeIngredientId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        menuItemService.deleteRecipeIngredientById(branchId, recipeIngredientId);
        return ResponseEntity.noContent().build();
    }
}