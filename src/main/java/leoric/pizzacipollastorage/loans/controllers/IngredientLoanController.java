package leoric.pizzacipollastorage.loans.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchServiceAccess;
import leoric.pizzacipollastorage.loans.dtos.IngredientLoanCreateDto;
import leoric.pizzacipollastorage.loans.dtos.IngredientLoanPatchDto;
import leoric.pizzacipollastorage.loans.dtos.IngredientLoanResponseDto;
import leoric.pizzacipollastorage.loans.services.IngredientLoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static leoric.pizzacipollastorage.PizzaCipollaStorageApplication.BRANCH_EMPLOYEE;
import static leoric.pizzacipollastorage.PizzaCipollaStorageApplication.BRANCH_MANAGER;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@Slf4j
public class IngredientLoanController {

    private final IngredientLoanService ingredientLoanService;
    private final BranchServiceAccess branchServiceAccess;

    // =========================
    // Endpoints s @PreAuthorize (jen MANAGER + ADMIN)
    // =========================
    @PostMapping("/{branchId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<IngredientLoanResponseDto> ingredientLoanCreate(
            @PathVariable UUID branchId,
            @RequestBody @Valid IngredientLoanCreateDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        return ResponseEntity.status(HttpStatus.CREATED).body(ingredientLoanService.createLoan(branchId, dto));
    }

    @PatchMapping("/{branchId}/{id}/return")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<IngredientLoanResponseDto> ingredientLoanReturn(
            @PathVariable UUID branchId,
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        return ResponseEntity.ok(ingredientLoanService.markLoanAsReturned(branchId, id));
    }

    @PatchMapping("/{branchId}/{id}/cancel")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<IngredientLoanResponseDto> ingredientLoanCancel(
            @PathVariable UUID branchId,
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        return ResponseEntity.ok(ingredientLoanService.markLoanAsCancelled(branchId, id));
    }

    @PatchMapping("/{branchId}/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<IngredientLoanResponseDto> ingredientLoanPatch(
            @PathVariable UUID branchId,
            @PathVariable UUID id,
            @RequestBody @Valid IngredientLoanPatchDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        return ResponseEntity.ok(ingredientLoanService.patchLoan(branchId, id, dto));
    }

    @DeleteMapping("/{branchId}/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<Void> ingredientLoanDelete(
            @PathVariable UUID branchId,
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER);
        ingredientLoanService.deleteLoan(branchId, id);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // Endpointy bez @PreAuthorize (BRANCH_MANAGER + ADMIN + BRANCH_EMPLOYEE)
    // =========================
    @GetMapping("/{branchId}/all")
    public ResponseEntity<Page<IngredientLoanResponseDto>> ingredientLoanGetAll(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) String search,
            @Parameter(required = false)
            @ParameterObject Pageable pageable
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER + ";" + BRANCH_EMPLOYEE);
        return ResponseEntity.ok(ingredientLoanService.getAllLoans(branchId, search, pageable));
    }

    @GetMapping("/{branchId}/{id}")
    public ResponseEntity<IngredientLoanResponseDto> ingredientLoanGetById(
            @PathVariable UUID branchId,
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER + ";" + BRANCH_EMPLOYEE);
        return ResponseEntity.ok(ingredientLoanService.getLoanById(branchId, id));
    }
}