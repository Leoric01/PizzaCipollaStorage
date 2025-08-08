package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.StockEntry.StockEntryCreateDto;
import leoric.pizzacipollastorage.DTOs.StockEntry.StockEntryResponseDto;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchServiceAccess;
import leoric.pizzacipollastorage.services.interfaces.StockEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/stock-entries")
@RequiredArgsConstructor
public class StockEntryController {

    private final StockEntryService stockEntryService;
    private final BranchServiceAccess branchServiceAccess;

    @PostMapping("/{branchId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<StockEntryResponseDto> stockEntryCreate(
            @PathVariable UUID branchId,
            @RequestBody StockEntryCreateDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(stockEntryService.createStockEntry(branchId, dto));
    }
}