package leoric.pizzacipollastorage.loans.controllers;

import jakarta.validation.Valid;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchServiceAccess;
import leoric.pizzacipollastorage.loans.dtos.IngredientLoanCreateDto;
import leoric.pizzacipollastorage.loans.dtos.IngredientLoanResponseDto;
import leoric.pizzacipollastorage.services.interfaces.IngredientLoanService;
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

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@Slf4j
public class IngredientLoanController {

    private final IngredientLoanService ingredientLoanService;
    private final BranchServiceAccess branchServiceAccess;

    @PostMapping("/{branchId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<IngredientLoanResponseDto> ingredientLoanCreate(
            @PathVariable UUID branchId,
            @RequestBody @Valid IngredientLoanCreateDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(ingredientLoanService.createLoan(branchId, dto));
    }

    @PatchMapping("/{branchId}/{id}/return")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<IngredientLoanResponseDto> ingredientLoanReturn(
            @PathVariable UUID branchId,
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(ingredientLoanService.markLoanAsReturned(branchId, id));
    }

    @GetMapping("/{branchId}/all")
    public ResponseEntity<Page<IngredientLoanResponseDto>> ingredientLoanGetAll(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) String search,
            @ParameterObject Pageable pageable
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(ingredientLoanService.getAllLoans(branchId, search, pageable));
    }

    @GetMapping("/{branchId}/{id}")
    public ResponseEntity<IngredientLoanResponseDto> ingredientLoanGetById(
            @PathVariable UUID branchId,
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(ingredientLoanService.getLoanById(branchId, id));
    }

//    @PatchMapping("/{branchId}/{id}")
//    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
//    public ResponseEntity<IngredientLoanResponseDto> loanPatch(
//            @PathVariable UUID branchId,
//            @PathVariable UUID id,
//            @RequestBody @Valid IngredientLoanPatchDto dto,
//            @AuthenticationPrincipal User currentUser
//    ) {
//        branchServiceAccess.assertHasAccess(branchId, currentUser);
//        return ResponseEntity.ok(ingredientLoanService.patchLoan(branchId, id, dto));
//    }
//
//    @DeleteMapping("/{branchId}/{id}")
//    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
//    public ResponseEntity<Void> loanDelete(
//            @PathVariable UUID branchId,
//            @PathVariable UUID id,
//            @AuthenticationPrincipal User currentUser
//    ) {
//        branchServiceAccess.assertHasAccess(branchId, currentUser);
//        ingredientLoanService.deleteLoan(branchId, id);
//        return ResponseEntity.noContent().build();
//    }
}