package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemCategoryCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemCategoryResponseDto;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchServiceAccess;
import leoric.pizzacipollastorage.services.interfaces.MenuItemCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/menu-item-category")
@RequiredArgsConstructor
public class MenuItemCategoryController {

    private final MenuItemCategoryService menuItemCategoryService;
    private final BranchServiceAccess branchServiceAccess;

    @PostMapping("/{branchId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<MenuItemCategoryResponseDto> menuItemCategoryCreate(
            @PathVariable UUID branchId,
            @RequestBody MenuItemCategoryCreateDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(menuItemCategoryService.menuItemCategoryAdd(branchId, dto));
    }

    @PostMapping("/{branchId}/bulk")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<List<MenuItemCategoryResponseDto>> menuItemCategoryCreateBulk(
            @PathVariable UUID branchId,
            @RequestBody List<MenuItemCategoryCreateDto> dtos,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(menuItemCategoryService.menuItemCategoryAddBulk(branchId, dtos));
    }

    @GetMapping("/{branchId}")
    public ResponseEntity<List<MenuItemCategoryResponseDto>> menuItemCategoryGetAll(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(menuItemCategoryService.menuItemCategoryFindAll(branchId));
    }

    @GetMapping("/by-id/{menuItemCategoryId}")
    public ResponseEntity<MenuItemCategoryResponseDto> menuItemCategoryGetById(
            @PathVariable UUID menuItemCategoryId
    ) {
        return ResponseEntity.ok(menuItemCategoryService.menuItemCategoryFindById(menuItemCategoryId));
    }

    @PutMapping("/{branchId}/{menuItemCategoryId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<MenuItemCategoryResponseDto> menuItemCategoryUpdateById(
            @PathVariable UUID branchId,
            @PathVariable UUID menuItemCategoryId,
            @RequestBody MenuItemCategoryCreateDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(menuItemCategoryService.menuItemCategoryUpdate(branchId, menuItemCategoryId, dto));
    }

    @DeleteMapping("/{branchId}/{menuItemCategoryId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<Void> menuItemCategoryDeleteById(
            @PathVariable UUID branchId,
            @PathVariable UUID menuItemCategoryId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        menuItemCategoryService.menuItemCategoryDelete(branchId, menuItemCategoryId);
        return ResponseEntity.noContent().build();
    }
}