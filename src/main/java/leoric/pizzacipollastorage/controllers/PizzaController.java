package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.Pizza.PizzaCreateDto;
import leoric.pizzacipollastorage.DTOs.Pizza.PizzaResponseDto;
import leoric.pizzacipollastorage.DTOs.Pizza.RecipeIngredientCreateDto;
import leoric.pizzacipollastorage.models.Pizza;
import leoric.pizzacipollastorage.models.RecipeIngredient;
import leoric.pizzacipollastorage.services.interfaces.PizzaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<RecipeIngredient> addIngredientToPizza(@RequestBody RecipeIngredientCreateDto dto) {
        return ResponseEntity.ok(pizzaService.addIngredientToPizza(dto));
    }

    @GetMapping
    public ResponseEntity<List<PizzaResponseDto>> getAllPizzas() {
        return ResponseEntity.ok(pizzaService.getAllPizzas());
    }
}