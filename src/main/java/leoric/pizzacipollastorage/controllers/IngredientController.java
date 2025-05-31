package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.IngredientCreateDto;
import leoric.pizzacipollastorage.DTOs.IngredientResponseDto;
import leoric.pizzacipollastorage.services.interfaces.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientService ingredientService;

    @PostMapping
    public ResponseEntity<IngredientResponseDto> createIngredient(@RequestBody IngredientCreateDto dto) {
        return ResponseEntity.ok(ingredientService.createIngredient(dto));
    }

    @GetMapping
    public ResponseEntity<List<IngredientResponseDto>> getAllIngredients() {
        return ResponseEntity.ok(ingredientService.getAllIngredients());
    }
}