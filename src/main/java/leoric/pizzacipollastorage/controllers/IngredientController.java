package leoric.pizzacipollastorage.controllers;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientAlias.IngredientAliasOverviewDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientCreateDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientResponseDto;
import leoric.pizzacipollastorage.services.interfaces.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientService ingredientService;

    @PutMapping("/{id}")
    public ResponseEntity<IngredientResponseDto> updateIngredientById(
            @PathVariable UUID id,
            @RequestBody IngredientCreateDto dto) {
        IngredientResponseDto updated = ingredientService.updateIngredient(id, dto);
        return ResponseEntity.ok(updated);
    }
    @PostMapping
    public ResponseEntity<IngredientResponseDto> createIngredient(@RequestBody IngredientCreateDto dto) {
        return ResponseEntity.ok(ingredientService.createIngredient(dto));
    }

    @GetMapping
    public ResponseEntity<List<IngredientResponseDto>> getAllIngredients() {
        return ResponseEntity.ok(ingredientService.getAllIngredients());
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<IngredientAliasOverviewDto> getAliasOverviewByName(@PathVariable String name) {
        return ingredientService.getAliasOverviewByName(name)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found for name or alias: " + name));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredientById(@PathVariable UUID id) {
        ingredientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}