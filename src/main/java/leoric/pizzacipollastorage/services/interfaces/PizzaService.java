package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.Pizza.*;
import leoric.pizzacipollastorage.models.Pizza;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PizzaService {

    Pizza createPizza(PizzaCreateDto dto);

    RecipeIngredientShortDto addIngredientToPizza(RecipeIngredientCreateDto dto);

    List<PizzaResponseDto> getAllPizzas();

    List<RecipeIngredientShortDto> addIngredientsToPizzaBulk(BulkRecipeCreateDto dto);

    PizzaResponseDto getPizzaById(UUID id);
}