package leoric.pizzacipollastorage.controllers;

import jakarta.validation.Valid;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleLastTimestampResponseDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleLastTimestampUpsertDto;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchServiceAccess;
import leoric.pizzacipollastorage.services.interfaces.MenuItemSaleLastTimestampService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static leoric.pizzacipollastorage.PizzaCipollaStorageApplication.BRANCH_EMPLOYEE;
import static leoric.pizzacipollastorage.PizzaCipollaStorageApplication.BRANCH_MANAGER;

@RestController
@RequestMapping("/api/sale-timestamp")
@RequiredArgsConstructor
public class MenuItemSaleLastTimestampController {

    private final MenuItemSaleLastTimestampService menuItemSaleLastTimestampService;
    private final BranchServiceAccess branchServiceAccess;

    @GetMapping("/{branchId}")
    public ResponseEntity<MenuItemSaleLastTimestampResponseDto> saleTimestampGetByBranch(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER + ";" + BRANCH_EMPLOYEE);
        return ResponseEntity.ok(menuItemSaleLastTimestampService.saleTimestampGetByBranch(branchId));
    }

    @PutMapping("/{branchId}")
    public ResponseEntity<MenuItemSaleLastTimestampResponseDto> saleTimestampUpsert(
            @PathVariable UUID branchId,
            @RequestBody @Valid MenuItemSaleLastTimestampUpsertDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        return ResponseEntity.ok(menuItemSaleLastTimestampService.saleTimestampUpsert(branchId, dto));
    }

//    @PostMapping("/{branchId}")
//    public ResponseEntity<MenuItemSaleLastTimestampResponseDto> saleTimestampCreate(
//            @PathVariable UUID branchId,
//            @RequestBody @Valid MenuItemSaleLastTimestampUpsertDto dto,
//            @AuthenticationPrincipal User currentUser
//    ) {
//        branchServiceAccess.assertHasAccess(branchId, currentUser);
//        return ResponseEntity.ok(menuItemSaleLastTimestampService.saleTimestampCreate(branchId, dto));
//    }
//
//    @PatchMapping("/{branchId}")
//    public ResponseEntity<MenuItemSaleLastTimestampResponseDto> saleTimestampUpdate(
//            @PathVariable UUID branchId,
//            @RequestBody @Valid MenuItemSaleLastTimestampUpdateDto dto,
//            @AuthenticationPrincipal User currentUser
//    ) {
//        branchServiceAccess.assertHasAccess(branchId, currentUser);
//        return ResponseEntity.ok(menuItemSaleLastTimestampService.saleTimestampUpdate(branchId, dto));
//    }
}