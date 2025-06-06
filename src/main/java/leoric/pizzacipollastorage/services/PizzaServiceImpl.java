package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.Pizza.*;
import leoric.pizzacipollastorage.handler.exceptions.MissingQuantityException;
import leoric.pizzacipollastorage.mapstruct.PizzaMapper;
import leoric.pizzacipollastorage.mapstruct.RecipeIngredientMapper;
import leoric.pizzacipollastorage.models.DishSize;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.Pizza;
import leoric.pizzacipollastorage.models.RecipeIngredient;
import leoric.pizzacipollastorage.repositories.DishSizeRepository;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.repositories.PizzaRepository;
import leoric.pizzacipollastorage.repositories.RecipeIngredientRepository;
import leoric.pizzacipollastorage.services.interfaces.IngredientAliasService;
import leoric.pizzacipollastorage.services.interfaces.PizzaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PizzaServiceImpl implements PizzaService {

    private final PizzaRepository pizzaRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final DishSizeRepository dishSizeRepository;
    private final RecipeIngredientMapper recipeIngredientMapper;

    private final IngredientAliasService ingredientAliasService;

    private final PizzaMapper pizzaMapper;

    @Override
    public List<RecipeIngredientShortDto> addIngredientsToPizzaBulk(BulkRecipeCreateDto dto) {
        Pizza pizza = pizzaRepository.findByName(dto.getPizzaName())
                .orElseThrow(() -> new EntityNotFoundException("Pizza not found: " + dto.getPizzaName()));

        // Určení velikosti porce
        int dishSizeId = dto.getDishSizeId() != null ? dto.getDishSizeId() : 1;

        // Načtení DishSize entity
        DishSize dishSize = dishSizeRepository.findById((long) dishSizeId)
                .orElseThrow(() -> new EntityNotFoundException("Dish size not found: ID = " + dishSizeId));

        float dishFactor = dishSizeId == 1 ? 1.0f : dishSize.getFactor();

        List<RecipeIngredientShortDto> created = new ArrayList<>();

        for (BulkRecipeIngredientDto ing : dto.getIngredients()) {
            // Najit ingredienci podle názvu nebo aliasu
            Ingredient ingredient = ingredientAliasService.findIngredientByNameFlexible(ing.getIngredientName())
                    .orElseThrow(() -> new EntityNotFoundException("Ingredient or alias not found: " + ing.getIngredientName()));

            float quantity;

            if (dishSizeId == 1) {
                if (ing.getQuantity() == null) {
                    throw new MissingQuantityException("Quantity is required for ingredient '" + ing.getIngredientName() + "' when dishSizeId is 1 (base size).");
                }
                quantity = ing.getQuantity();
            } else {
                Optional<RecipeIngredient> base = recipeIngredientRepository
                        .findByPizzaIdAndIngredientIdAndDishSizeId(pizza.getId(), ingredient.getId(), 1L);

                if (base.isPresent()) {
                    quantity = base.get().getQuantity() * dishFactor;
                } else if (ing.getQuantity() != null) {
                    quantity = ing.getQuantity();
                } else {
                    throw new MissingQuantityException("Missing quantity for ingredient '" + ing.getIngredientName() + "' and no base recipe found (dishSizeId=1).");
                }
            }

            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setPizza(pizza);
            recipeIngredient.setIngredient(ingredient);
            recipeIngredient.setQuantity(quantity);
            recipeIngredient.setDishSize(dishSize);

            created.add(recipeIngredientMapper.toShortDto(recipeIngredientRepository.save(recipeIngredient)));
        }
        return created;
    }

    public Pizza createPizza(PizzaCreateDto dto) {
        Pizza pizza = new Pizza();
        pizza.setName(dto.getName());
        pizza.setDescription(dto.getDescription());
        return pizzaRepository.save(pizza);
    }

    public RecipeIngredientShortDto addIngredientToPizza(RecipeIngredientCreateDto dto) {
        Pizza pizza = pizzaRepository.findById(dto.getPizzaId())
                .orElseThrow(() -> new EntityNotFoundException("Pizza not found"));
        Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId())
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setPizza(pizza);
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setQuantity(dto.getQuantity());

        return recipeIngredientMapper.toShortDto(recipeIngredientRepository.save(recipeIngredient));
    }

    @Override
    public List<PizzaResponseDto> getAllPizzas() {
        return pizzaMapper.toDtoList(pizzaRepository.findAll());
    }
}