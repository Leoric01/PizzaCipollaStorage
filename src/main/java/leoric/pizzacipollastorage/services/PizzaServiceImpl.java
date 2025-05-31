package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.PizzaCreateDto;
import leoric.pizzacipollastorage.DTOs.PizzaResponseDto;
import leoric.pizzacipollastorage.DTOs.RecipeIngredientCreateDto;
import leoric.pizzacipollastorage.mapstruct.PizzaMapper;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.Pizza;
import leoric.pizzacipollastorage.models.RecipeIngredient;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.repositories.PizzaRepository;
import leoric.pizzacipollastorage.repositories.RecipeIngredientRepository;
import leoric.pizzacipollastorage.services.interfaces.PizzaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PizzaServiceImpl implements PizzaService {
    private final PizzaRepository pizzaRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final PizzaMapper pizzaMapper;


    public Pizza createPizza(PizzaCreateDto dto) {
        Pizza pizza = new Pizza();
        pizza.setName(dto.getName());
        pizza.setDescription(dto.getDescription());
        return pizzaRepository.save(pizza);
    }

    public RecipeIngredient addIngredientToPizza(RecipeIngredientCreateDto dto) {
        Pizza pizza = pizzaRepository.findById(dto.getPizzaId())
                .orElseThrow(() -> new EntityNotFoundException("Pizza not found"));
        Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId())
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setPizza(pizza);
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setQuantity(dto.getQuantity());
        return recipeIngredientRepository.save(recipeIngredient);
    }
    @Override
    public List<PizzaResponseDto> getAllPizzas() {
        return pizzaMapper.toDtoList(pizzaRepository.findAll());
    }
}