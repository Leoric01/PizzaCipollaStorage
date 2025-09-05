package leoric.pizzacipollastorage.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemResponseDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.*;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchServiceAccess;
import leoric.pizzacipollastorage.services.interfaces.IgnoredThirdPartyNameService;
import leoric.pizzacipollastorage.services.interfaces.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static leoric.pizzacipollastorage.PizzaCipollaStorageApplication.BRANCH_EMPLOYEE;
import static leoric.pizzacipollastorage.PizzaCipollaStorageApplication.BRANCH_MANAGER;

@RestController
@RequestMapping("/api/menuitems-sales/map")
@RequiredArgsConstructor
public class MenuItemSaleMapperController {

    private final IgnoredThirdPartyNameService ignoredThirdPartyNameService;
    private final BranchServiceAccess branchServiceAccess;
    private final MenuItemService menuItemService;

    // =========================
    // POST – zápis (BRANCH_MANAGER + ADMIN)
    // =========================

    @PostMapping("/{branchId}/ignored-names")
    public ResponseEntity<Void> ignoredNameAdd(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody IgnoredThirdPartyNameCreateDto dto
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        ignoredThirdPartyNameService.addIgnoredName(branchId, dto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{branchId}/ignored-names/bulk")
    public ResponseEntity<Void> ignoredNamesAddBulk(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody IgnoredThirdPartyNameBulkCreateDto dto
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        ignoredThirdPartyNameService.addIgnoredNameBulk(branchId, dto.getNames());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{branchId}/ignored-names/{name}")
    public ResponseEntity<Void> ignoredNameRemove(
            @PathVariable UUID branchId,
            @PathVariable String name,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        ignoredThirdPartyNameService.removeIgnoredName(branchId, name);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{branchId}/ignored-names")
    public ResponseEntity<List<String>> ignoredNamesGetAllByBranchId(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER + ";" + BRANCH_EMPLOYEE);
        List<String> ignored = ignoredThirdPartyNameService.listIgnoredNames(branchId);
        return ResponseEntity.ok(ignored);
    }

    @PostMapping("/{branchId}/map-third-party-names")
    public ResponseEntity<ThirdPartyNameMappingResponseDto> menuItemMapThirdPartyNames(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser,
            @RequestBody List<String> thirdPartyNames
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        ThirdPartyNameMappingResponseDto thirdPartyNameMappingResponseDto = menuItemService.mapThirdPartyNames(branchId, thirdPartyNames);
        return ResponseEntity.ok(thirdPartyNameMappingResponseDto);
    }

    @PostMapping("/{branchId}/{menuItemId}/third-party-names")
    public ResponseEntity<Void> thirdPartyNameAdd(
            @PathVariable UUID branchId,
            @PathVariable UUID menuItemId,
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody MenuItemThirdPartyNameCreateDto dto
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        menuItemService.addThirdPartyName(menuItemId, branchId, dto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{branchId}/{menuItemId}/third-party-names")
    public ResponseEntity<Void> thirdPartyNameUpdate(
            @PathVariable UUID branchId,
            @PathVariable UUID menuItemId,
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody MenuItemThirdPartyNameUpdateDto dto
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        menuItemService.updateThirdPartyName(menuItemId, branchId, dto.getOldName(), dto.getNewName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{branchId}/{menuItemId}/third-party-names")
    public ResponseEntity<Void> thirdPartyNameDelete(
            @PathVariable UUID branchId,
            @PathVariable UUID menuItemId,
            @AuthenticationPrincipal User currentUser,
            @RequestParam String name
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        menuItemService.deleteThirdPartyName(menuItemId, branchId, name);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // GET – čtení (BRANCH_MANAGER + ADMIN + BRANCH_EMPLOYEE)
    // =========================
    @GetMapping("/{branchId}/map-names")
    public ResponseEntity<Page<MenuItemMapNameResponseDto>> menuItemGetAllThirdPartyNames(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) String search,
            @ParameterObject @Parameter(required = false) Pageable pageable
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER + ";" + BRANCH_EMPLOYEE);
        return ResponseEntity.ok(menuItemService.menuItemGetAllMapNames(branchId, search, pageable));
    }

    @GetMapping("/{branchId}/names-with-sizes")
    public ResponseEntity<List<MenuItemNameWithSizesDto>> menuItemGetThirdPartyNamesWithSizes(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER + ";" + BRANCH_EMPLOYEE);
        return ResponseEntity.ok(menuItemService.getMenuItemNamesWithSizes(branchId));
    }

    @GetMapping("/{branchId}/by-third-party-name")
    public ResponseEntity<List<MenuItemResponseDto>> menuItemsByThirdPartyNameGet(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser,
            @RequestParam String thirdPartyName
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER + ";" + BRANCH_EMPLOYEE);
        return ResponseEntity.ok(menuItemService.getMenuItemsByThirdPartyName(branchId, thirdPartyName));
    }
}