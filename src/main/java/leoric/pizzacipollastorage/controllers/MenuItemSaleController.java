package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleBulkCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleResponseDto;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchServiceAccess;
import leoric.pizzacipollastorage.services.interfaces.MenuItemSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/menuitems-sales")
@RequiredArgsConstructor
public class MenuItemSaleController {

    private final MenuItemSaleService menuItemSaleService;
    private final BranchServiceAccess branchServiceAccess;

    @PostMapping("/{branchId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN', 'EMPLOYEE')")
    public ResponseEntity<MenuItemSaleResponseDto> saleCreate(
            @PathVariable UUID branchId,
            @RequestBody MenuItemSaleCreateDto menuItemSaleCreateDto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        MenuItemSaleResponseDto response = menuItemSaleService.createSale(branchId, menuItemSaleCreateDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{branchId}/bulk")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN', 'EMPLOYEE')")
    public ResponseEntity<List<MenuItemSaleResponseDto>> saleCreateBulk(
            @PathVariable UUID branchId,
            @RequestBody MenuItemSaleBulkCreateDto menuItemSaleBulkCreateDto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        List<MenuItemSaleResponseDto> responses = menuItemSaleService.createSaleBulk(branchId, menuItemSaleBulkCreateDto);
        return ResponseEntity.ok(responses);
    }

}