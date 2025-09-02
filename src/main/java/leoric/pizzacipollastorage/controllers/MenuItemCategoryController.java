package leoric.pizzacipollastorage.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemCategoryCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemCategoryResponseDto;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchServiceAccess;
import leoric.pizzacipollastorage.services.interfaces.MenuItemCategoryService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static leoric.pizzacipollastorage.PizzaCipollaStorageApplication.*;

@RestController
@RequestMapping("/api/menu-item-category")
@RequiredArgsConstructor
public class MenuItemCategoryController {

    private final MenuItemCategoryService menuItemCategoryService;
    private final BranchServiceAccess branchServiceAccess;

    // =========================
    // Endpoints s @PreAuthorize (jen MANAGER + ADMIN)
    // =========================
    @PostMapping("/{branchId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<MenuItemCategoryResponseDto> menuItemCategoryCreate(
            @PathVariable UUID branchId,
            @RequestBody MenuItemCategoryCreateDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER + ";" + ADMIN);
        return ResponseEntity.ok(menuItemCategoryService.menuItemCategoryAdd(branchId, dto));
    }

    @PostMapping("/{branchId}/bulk")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<List<MenuItemCategoryResponseDto>> menuItemCategoryCreateBulk(
            @PathVariable UUID branchId,
            @RequestBody List<MenuItemCategoryCreateDto> dtos,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER + ";" + ADMIN);
        return ResponseEntity.ok(menuItemCategoryService.menuItemCategoryAddBulk(branchId, dtos));
    }

    @PutMapping("/{branchId}/{menuItemCategoryId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<MenuItemCategoryResponseDto> menuItemCategoryUpdateById(
            @PathVariable UUID branchId,
            @PathVariable UUID menuItemCategoryId,
            @RequestBody MenuItemCategoryCreateDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER + ";" + ADMIN);
        return ResponseEntity.ok(menuItemCategoryService.menuItemCategoryUpdate(branchId, menuItemCategoryId, dto));
    }

    @DeleteMapping("/{branchId}/{menuItemCategoryId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<Void> menuItemCategoryDeleteById(
            @PathVariable UUID branchId,
            @PathVariable UUID menuItemCategoryId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER + ";" + ADMIN);
        menuItemCategoryService.menuItemCategoryDelete(branchId, menuItemCategoryId);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // Endpointy bez @PreAuthorize (BRANCH_MANAGER + ADMIN + BRANCH_EMPLOYEE)
    // =========================
    @GetMapping("/{branchId}")
    public ResponseEntity<Page<MenuItemCategoryResponseDto>> menuItemCategoryGetAll(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) String search,
            @ParameterObject
            @Parameter(required = false) Pageable pageable
    ) {
        branchServiceAccess.assertHasRoleOnBranch(
                branchId,
                currentUser,
                BRANCH_MANAGER + ";" + ADMIN + ";" + BRANCH_EMPLOYEE
        );
        return ResponseEntity.ok(menuItemCategoryService.menuItemCategoryGetAll(branchId, search, pageable));
    }

    @GetMapping("/{branchId}/{menuItemCategoryId}")
    public ResponseEntity<MenuItemCategoryResponseDto> menuItemCategoryGetById(
            @PathVariable UUID branchId,
            @PathVariable UUID menuItemCategoryId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(
                branchId,
                currentUser,
                BRANCH_MANAGER + ";" + ADMIN + ";" + BRANCH_EMPLOYEE
        );
        return ResponseEntity.ok(menuItemCategoryService.menuItemCategoryFindById(menuItemCategoryId));
    }
}