package leoric.pizzacipollastorage.purchase.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchServiceAccess;
import leoric.pizzacipollastorage.purchase.dtos.PurchaseInvoice.PurchaseInvoiceCreateDto;
import leoric.pizzacipollastorage.purchase.dtos.PurchaseInvoice.PurchaseInvoiceResponseDto;
import leoric.pizzacipollastorage.purchase.services.PurchaseInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static leoric.pizzacipollastorage.PizzaCipollaStorageApplication.BRANCH_EMPLOYEE;
import static leoric.pizzacipollastorage.PizzaCipollaStorageApplication.BRANCH_MANAGER;

@RestController
@RequestMapping("/api/purchase-invoices")
@RequiredArgsConstructor
public class PurchaseInvoiceController {

    private final PurchaseInvoiceService purchaseInvoiceService;
    private final BranchServiceAccess branchServiceAccess;

    @PostMapping("/{branchId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<PurchaseInvoiceResponseDto> purchaseInvoiceCreate(
            @PathVariable UUID branchId,
            @RequestBody @Valid PurchaseInvoiceCreateDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        return ResponseEntity.ok(purchaseInvoiceService.createInvoice(branchId, dto));
    }

    @PostMapping("/{branchId}/{invoiceId}/stock")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<Void> purchaseInvoiceStockFromInvoice(
            @PathVariable UUID branchId,
            @PathVariable UUID invoiceId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        purchaseInvoiceService.stockFromInvoice(branchId, invoiceId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{branchId}/{invoiceId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<PurchaseInvoiceResponseDto> purchaseInvoiceUpdate(
            @PathVariable UUID branchId,
            @PathVariable UUID invoiceId,
            @RequestBody @Valid PurchaseInvoiceCreateDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        return ResponseEntity.ok(purchaseInvoiceService.update(branchId, invoiceId, dto));
    }

    @DeleteMapping("/{branchId}/{invoiceId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<Void> purchaseInvoiceDelete(
            @PathVariable UUID branchId,
            @PathVariable UUID invoiceId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        purchaseInvoiceService.delete(branchId, invoiceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{branchId}/all")
    public ResponseEntity<Page<PurchaseInvoiceResponseDto>> purchaseInvoiceGetAll(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser,
            @ParameterObject
            @Parameter(required = false) Pageable pageable
    ) {
        branchServiceAccess.assertHasRoleOnBranch(
                branchId,
                currentUser,
                BRANCH_MANAGER + ";" + BRANCH_EMPLOYEE
        );
        return ResponseEntity.ok(purchaseInvoiceService.getAll(branchId, pageable));
    }

    @GetMapping("/{branchId}/{invoiceId}")
    public ResponseEntity<PurchaseInvoiceResponseDto> purchaseInvoiceGetById(
            @PathVariable UUID branchId,
            @PathVariable UUID invoiceId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(
                branchId,
                currentUser,
                BRANCH_MANAGER + ";" + BRANCH_EMPLOYEE
        );
        return ResponseEntity.ok(purchaseInvoiceService.getById(branchId, invoiceId));
    }
}