package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.StockEntry.StockEntryCreateDto;
import leoric.pizzacipollastorage.DTOs.StockEntry.StockEntryResponseDto;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchServiceAccess;
import leoric.pizzacipollastorage.services.interfaces.StockEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static leoric.pizzacipollastorage.PizzaCipollaStorageApplication.*;

@RestController
@RequestMapping("/api/stock-entries")
@RequiredArgsConstructor
public class StockEntryController {

    private final StockEntryService stockEntryService;
    private final BranchServiceAccess branchServiceAccess;

    @PostMapping("/{branchId}")
    public ResponseEntity<StockEntryResponseDto> stockEntryCreate(
            @PathVariable UUID branchId,
            @RequestBody StockEntryCreateDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER + ";" + ADMIN + ";" + BRANCH_EMPLOYEE);
        return ResponseEntity.ok(stockEntryService.stockEntryCreate(branchId, dto));
    }

    @PostMapping("/{branchId}/bulk")
    public ResponseEntity<List<StockEntryResponseDto>> stockEntryBulkCreate(
            @PathVariable UUID branchId,
            @RequestBody List<StockEntryCreateDto> dtos,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasRoleOnBranch(branchId, currentUser, BRANCH_MANAGER + ";" + ADMIN + ";" + BRANCH_EMPLOYEE);
        return ResponseEntity.ok(stockEntryService.createStockEntries(branchId, dtos));
    }
}