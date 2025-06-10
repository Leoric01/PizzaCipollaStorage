package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientAlias.IngredientAliasDto;
import leoric.pizzacipollastorage.services.interfaces.IngredientAliasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/ingredient-aliases")
@RequiredArgsConstructor
public class IngredientAliasController {

    private final IngredientAliasService ingredientAliasService;

    @PostMapping
    public ResponseEntity<IngredientAliasDto> addAlias(@RequestBody IngredientAliasDto dto) {
        return ResponseEntity.ok(ingredientAliasService.addAlias(dto));
    }

    @GetMapping("/ingredient/{ingredientId}")
    public ResponseEntity<List<IngredientAliasDto>> getAliases(@PathVariable UUID ingredientId) {
        return ResponseEntity.ok(ingredientAliasService.getAliasesByIngredientId(ingredientId));
    }

    @DeleteMapping("/{aliasId}")
    public ResponseEntity<Void> deleteAlias(@PathVariable UUID aliasId) {
        ingredientAliasService.deleteAlias(aliasId);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/batch")
    public ResponseEntity<List<IngredientAliasDto>> addAliases(@RequestBody List<IngredientAliasDto> dtos) {
        List<IngredientAliasDto> result = dtos.stream()
                .map(ingredientAliasService::addAlias)
                .filter(Objects::nonNull)
                .toList();

        return ResponseEntity.ok(result);
    }
}