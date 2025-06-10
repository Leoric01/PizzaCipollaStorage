package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.Pizza.*;
import leoric.pizzacipollastorage.models.Pizza;
import leoric.pizzacipollastorage.services.interfaces.PizzaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pizzas")
@RequiredArgsConstructor
public class PizzaController {
    private final PizzaService pizzaService;

    @PostMapping
    public ResponseEntity<Pizza> createPizza(@RequestBody PizzaCreateDto dto) {
        return ResponseEntity.ok(pizzaService.createPizza(dto));
    }

    @PostMapping("/recipes")
    public ResponseEntity<RecipeIngredientShortDto> addIngredientToPizza(@RequestBody RecipeIngredientCreateDto dto) {
        return ResponseEntity.ok(pizzaService.addIngredientToPizza(dto));
    }

    @PostMapping("/recipes/bulk")
    public ResponseEntity<List<RecipeIngredientShortDto>> addIngredientsToPizzaBulk(@RequestBody BulkRecipeCreateDto dto) {
        return ResponseEntity.ok(pizzaService.addIngredientsToPizzaBulk(dto));
    }

    @GetMapping
    public ResponseEntity<List<PizzaResponseDto>> getAllPizzas() {
        return ResponseEntity.ok(pizzaService.getAllPizzas());
    }
    @GetMapping("/{id}")
    public ResponseEntity<PizzaResponseDto> getPizzaById(@PathVariable UUID id) {
        return ResponseEntity.ok(pizzaService.getPizzaById(id));
    }
}