package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.PizzaCreateDto;
import leoric.pizzacipollastorage.DTOs.PizzaResponseDto;
import leoric.pizzacipollastorage.DTOs.RecipeIngredientCreateDto;
import leoric.pizzacipollastorage.models.Pizza;
import leoric.pizzacipollastorage.models.RecipeIngredient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PizzaService {

    Pizza createPizza(PizzaCreateDto dto);

    RecipeIngredient addIngredientToPizza(RecipeIngredientCreateDto dto);

    List<PizzaResponseDto> getAllPizzas();
}