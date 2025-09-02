package leoric.pizzacipollastorage.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemResponseDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemMapNameResponseDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemNameWithSizesDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemThirdPartyNameCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemThirdPartyNameUpdateDto;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchServiceAccess;
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
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/menuitems-sales/map")
@RequiredArgsConstructor
public class MenuItemSaleMapperController {

    private final BranchServiceAccess branchServiceAccess;
    private final MenuItemService menuItemService;

    @PostMapping("/{branchId}/map-third-party-names")
    public ResponseEntity<Map<String, UUID>> menuItemMapThirdPartyNames(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser,
            @RequestBody List<String> thirdPartyNames
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        Map<String, UUID> mapping = menuItemService.mapThirdPartyNames(branchId, thirdPartyNames);
        return ResponseEntity.ok(mapping);
    }

    @GetMapping("/{branchId}/map-names")
    public ResponseEntity<Page<MenuItemMapNameResponseDto>> menuItemGetAllThirdPartyNames(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) String search,
            @ParameterObject @Parameter(required = false) Pageable pageable
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(menuItemService.menuItemGetAllMapNames(branchId, search, pageable));
    }

    @GetMapping("/{branchId}/names-with-sizes")
    public ResponseEntity<List<MenuItemNameWithSizesDto>> menuItemGetThirdPartyNamesWithSizes(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(menuItemService.getMenuItemNamesWithSizes(branchId));
    }

    @GetMapping("/{branchId}/by-third-party-name")
    public ResponseEntity<List<MenuItemResponseDto>> menuItemsByThirdPartyNameGet(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser,
            @RequestParam String thirdPartyName
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(
                menuItemService.getMenuItemsByThirdPartyName(branchId, thirdPartyName)
        );
    }

    @PostMapping("/{branchId}/{menuItemId}/third-party-names")
    public ResponseEntity<Void> thirdPartyNameAdd(
            @PathVariable UUID branchId,
            @PathVariable UUID menuItemId,
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody MenuItemThirdPartyNameCreateDto dto
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
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
        branchServiceAccess.assertHasAccess(branchId, currentUser);
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
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        menuItemService.deleteThirdPartyName(menuItemId, branchId, name);
        return ResponseEntity.noContent().build();
    }
}