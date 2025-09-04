package leoric.pizzacipollastorage.init;

import jakarta.annotation.PostConstruct;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.MenuItem;
import leoric.pizzacipollastorage.models.RecipeIngredient;
import leoric.pizzacipollastorage.repositories.MenuItemRepository;
import leoric.pizzacipollastorage.repositories.RecipeIngredientRepository;
import leoric.pizzacipollastorage.services.interfaces.IngredientAliasService;
import leoric.pizzacipollastorage.utils.CustomUtilityString;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@DependsOn({"menuItemInitializer", "ingredientInitializer"})
public class MenuItemRecipeInitializer {

    private final MenuItemRepository menuItemRepository;
    private final IngredientAliasService ingredientAliasService;
    private final RecipeIngredientRepository recipeIngredientRepository;

    @PostConstruct
    public void insertMenuItemRecipes() {

    }

    private void createRecipe(String pizzaName, Map<String, Float> ingredients) {
        Optional<MenuItem> pizzaOpt = menuItemRepository.findAll().stream()
                .filter(p -> CustomUtilityString.normalize(p.getName()).equals(CustomUtilityString.normalize(pizzaName)))
                .findFirst();

        if (pizzaOpt.isEmpty()) return;
        MenuItem menuItem = pizzaOpt.get();

        for (Map.Entry<String, Float> entry : ingredients.entrySet()) {
            String ingName = entry.getKey();
            float quantity = entry.getValue();

            Ingredient ingredient = ingredientAliasService.findIngredientByNameFlexible(ingName)
                    .orElseThrow(() -> new IllegalStateException("Ingredient not found (even with aliases): " + ingName));

            boolean exists = recipeIngredientRepository
                    .findByMenuItemId(menuItem.getId()).stream()
                    .anyMatch(r -> r.getIngredient().getId().equals(ingredient.getId()));

            if (!exists) {
                RecipeIngredient ri = RecipeIngredient.builder()
                        .menuItem(menuItem)
                        .ingredient(ingredient)
                        .quantity(quantity)
                        .build();

                recipeIngredientRepository.save(ri);
            }
        }
    }
}