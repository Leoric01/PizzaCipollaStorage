package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientCreateDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientResponseDto;
import leoric.pizzacipollastorage.services.interfaces.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ingredients/{branchId}")
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientService ingredientService;

    @PostMapping
    public ResponseEntity<IngredientResponseDto> createIngredient(
            @PathVariable UUID branchId,
            @RequestBody IngredientCreateDto dto
    ) {
        return ResponseEntity.ok(ingredientService.createIngredient(branchId, dto));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<IngredientResponseDto>> createIngredientsBulk(
            @PathVariable UUID branchId,
            @RequestBody List<IngredientCreateDto> dtos
    ) {
        return ResponseEntity.ok(ingredientService.createIngredientsBulk(branchId, dtos));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientResponseDto> updateIngredientById(
            @PathVariable UUID branchId,
            @PathVariable UUID id,
            @RequestBody IngredientCreateDto dto
    ) {
        return ResponseEntity.ok(ingredientService.updateIngredient(branchId, id, dto));
    }

    @GetMapping
    public ResponseEntity<List<IngredientResponseDto>> getAllIngredients(
            @PathVariable UUID branchId
    ) {
        return ResponseEntity.ok(ingredientService.getAllIngredients(branchId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredientById(@PathVariable UUID branchId, @PathVariable UUID id) {
        ingredientService.deleteById(branchId, id);
        return ResponseEntity.noContent().build();
    }

    //    @GetMapping("/by-name/{name}")
//    public ResponseEntity<IngredientAliasOverviewDto> getAliasOverviewByName(@PathVariable String name) {
//        return ingredientService.getAliasOverviewByName(name)
//                .map(ResponseEntity::ok)
//                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found for name or alias: " + name));
//    }
}