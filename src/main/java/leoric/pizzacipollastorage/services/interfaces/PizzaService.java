package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.Pizza.PizzaCreateDto;
import leoric.pizzacipollastorage.DTOs.Pizza.PizzaResponseDto;
import leoric.pizzacipollastorage.DTOs.Pizza.RecipeIngredientCreateDto;
import leoric.pizzacipollastorage.DTOs.Pizza.RecipeIngredientShortDto;
import leoric.pizzacipollastorage.models.Pizza;
import leoric.pizzacipollastorage.models.RecipeIngredient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PizzaService {

    Pizza createPizza(PizzaCreateDto dto);

    RecipeIngredientShortDto addIngredientToPizza(RecipeIngredientCreateDto dto);

    List<PizzaResponseDto> getAllPizzas();
}