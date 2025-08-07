package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientCreateDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientResponseDto;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchServiceAccess;
import leoric.pizzacipollastorage.services.interfaces.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ingredient")
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientService ingredientService;
    private final BranchServiceAccess branchServiceAccess;

    @GetMapping("/{branchId}/all")
    public ResponseEntity<List<IngredientResponseDto>> ingredientGetAll(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);

        return ResponseEntity.ok(ingredientService.ingredientGetAll(branchId));
    }

    @GetMapping("/{ingredientId}")
    public ResponseEntity<IngredientResponseDto> ingredientGetById(@PathVariable UUID ingredientId) {
        return ResponseEntity.ok(ingredientService.ingredientGetById(ingredientId));
    }

    @PostMapping("/{branchId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<IngredientResponseDto> ingredientCreate(
            @PathVariable UUID branchId,
            @RequestBody IngredientCreateDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);

        return ResponseEntity.ok(ingredientService.ingredientCreate(branchId, dto));
    }

    @PostMapping("/{branchId}/bulk")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<List<IngredientResponseDto>> ingredientCreateBulk(
            @PathVariable UUID branchId,
            @RequestBody List<IngredientCreateDto> dtos,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);

        return ResponseEntity.ok(ingredientService.ingredientCreateBulk(branchId, dtos));
    }

    @PutMapping("/{branchId}/{ingredientId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<IngredientResponseDto> ingredientUpdateById(
            @PathVariable UUID branchId,
            @PathVariable UUID ingredientId,
            @RequestBody IngredientCreateDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);

        return ResponseEntity.ok(ingredientService.ingredientUpdate(branchId, ingredientId, dto));
    }

    @DeleteMapping("/{branchId}/{ingredientId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<Void> ingredientDeleteById(@PathVariable UUID branchId,
                                                     @PathVariable UUID ingredientId,
                                                     @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);

        ingredientService.ingredientDeleteById(branchId, ingredientId);
        return ResponseEntity.noContent().build();
    }

    //    @GetMapping("/by-name/{name}")
//    public ResponseEntity<IngredientAliasOverviewDto> getAliasOverviewByName(@PathVariable String name) {
//        return ingredientService.getAliasOverviewByName(name)
//                .map(ResponseEntity::ok)
//                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found for name or alias: " + name));
//    }
}