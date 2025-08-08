package leoric.pizzacipollastorage.purchase.controllers;

import jakarta.validation.Valid;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchServiceAccess;
import leoric.pizzacipollastorage.purchase.dtos.Supplier.SupplierCreateDto;
import leoric.pizzacipollastorage.purchase.dtos.Supplier.SupplierResponseDto;
import leoric.pizzacipollastorage.purchase.services.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;
    private final BranchServiceAccess branchServiceAccess;

    @PostMapping("/{branchId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<SupplierResponseDto> supplierCreate(
            @PathVariable UUID branchId,
            @RequestBody SupplierCreateDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(supplierService.supplierCreate(branchId, dto));
    }

    @PostMapping("/{branchId}/bulk")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<List<SupplierResponseDto>> supplierCreateBulk(
            @PathVariable UUID branchId,
            @RequestBody List<SupplierCreateDto> dtos,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(supplierService.supplierCreateBulk(branchId, dtos));
    }

    @GetMapping("/{branchId}")
    public ResponseEntity<List<SupplierResponseDto>> supplierGetAll(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(supplierService.supplierGetAll(branchId));
    }

    @GetMapping("/{branchId}/{supplierId}")
    public ResponseEntity<SupplierResponseDto> supplierGetById(
            @PathVariable UUID branchId,
            @PathVariable UUID supplierId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(supplierService.supplierGetById(branchId, supplierId));
    }

    @PutMapping("/{branchId}/{supplierId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<SupplierResponseDto> supplierUpdate(
            @PathVariable UUID branchId,
            @PathVariable UUID supplierId,
            @RequestBody @Valid SupplierCreateDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(supplierService.supplierUpdate(branchId, supplierId, dto));
    }

    @DeleteMapping("/{branchId}/{supplierId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<Void> supplierDelete(
            @PathVariable UUID branchId,
            @PathVariable UUID supplierId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        supplierService.supplierDelete(branchId, supplierId);
        return ResponseEntity.noContent().build();
    }
}