package leoric.pizzacipollastorage.inventory.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchServiceAccess;
import leoric.pizzacipollastorage.inventory.dtos.Inventory.InventorySnapshotCreateDto;
import leoric.pizzacipollastorage.inventory.dtos.Inventory.InventorySnapshotResponseDto;
import leoric.pizzacipollastorage.inventory.services.InventoryService;
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

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final BranchServiceAccess branchServiceAccess;

    @PostMapping("/{branchId}/snapshot")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<InventorySnapshotResponseDto> inventoryCreateSnapshot(
            @PathVariable UUID branchId,
            @RequestBody InventorySnapshotCreateDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);

        return ResponseEntity.ok(inventoryService.createSnapshot(branchId, dto));
    }

    @PostMapping("/{branchId}/snapshot/bulk")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<List<InventorySnapshotResponseDto>> inventoryCreateSnapshotBulk(
            @PathVariable UUID branchId,
            @RequestBody List<InventorySnapshotCreateDto> dtos,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        // TODO VYTVORIT TY ACCESS ROLE
//        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, "MANAGER");

        return ResponseEntity.ok(inventoryService.createSnapshotBulk(branchId, dtos));
    }

    @GetMapping("/{branchId}/current-status")
    public ResponseEntity<Page<InventorySnapshotResponseDto>> inventoryStatusGetCurrent(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) String search,
            @ParameterObject
            @Parameter(required = false)
            Pageable pageable
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(inventoryService.getCurrentInventoryStatus(branchId, search, pageable));
    }
}