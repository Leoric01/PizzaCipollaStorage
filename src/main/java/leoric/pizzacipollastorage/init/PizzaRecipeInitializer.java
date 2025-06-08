package leoric.pizzacipollastorage.init;

import jakarta.annotation.PostConstruct;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.Pizza;
import leoric.pizzacipollastorage.models.RecipeIngredient;
import leoric.pizzacipollastorage.repositories.PizzaRepository;
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
@DependsOn({"pizzaInitializer", "ingredientInitializer"})
public class PizzaRecipeInitializer {

    private final PizzaRepository pizzaRepository;
    private final IngredientAliasService ingredientAliasService;
    private final RecipeIngredientRepository recipeIngredientRepository;

    @PostConstruct
    public void insertPizzaRecipes() {
        createRecipe("Margherita", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.11f,
                "Bazalka", 0.005f
        ));

        createRecipe("Quattro Formaggi", Map.of(
                "Mouka", 0.25f,
                "Smetana", 0.05f,
                "Mozzarella", 0.08f,
                "Mascarpone", 0.05f,
                "Gorgonzola", 0.05f,
                "Parmezán", 0.02f
        ));

        createRecipe("Margherita Bufala", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella di bufala", 0.11f,
                "Bazalka", 0.005f
        ));

        createRecipe("Ai Formaggi", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.08f,
                "Uzený eidam", 0.05f,
                "Gorgonzola", 0.05f
        ));

        createRecipe("Funghi", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.11f,
                "Žampiony", 0.09f
        ));


        createRecipe("Santoška", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Uzený eidam", 0.05f,
                "Camembert", 0.05f,
                "Rajčata", 0.06f,
                "Olivy zelené", 0.03f
        ));

        createRecipe("Spinaci", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.11f,
                "Špenát", 0.06f,
                "Vejce", 0.05f
        ));
        createRecipe("Bandiera", Map.of(
                "Mouka", 0.25f,
                "Smetana", 0.05f,
                "Mozzarella", 0.11f,
                "Rajčata", 0.06f,
                "Rukola", 0.015f,
                "Česnek", 0.005f
        ));

        createRecipe("Vesuvio", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.11f,
                "Šunka", 0.10f
        ));

        createRecipe("Capricciosa", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.11f,
                "Šunka", 0.08f,
                "Žampiony", 0.09f
        ));

        createRecipe("Hawaii", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.11f,
                "Šunka", 0.08f,
                "Ananas", 0.07f
        ));

        createRecipe("Al Capone", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.11f,
                "Šunka", 0.06f,
                "Salám", 0.05f,
                "Paprika", 0.05f
        ));

        createRecipe("Diavola", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.11f,
                "Šunka", 0.06f,
                "Žampiony", 0.08f,
                "Jalapeňos", 0.03f
        ));

        createRecipe("Alla Crema", Map.of(
                "Mouka", 0.25f,
                "Smetana", 0.05f,
                "Mozzarella", 0.11f,
                "Šunka", 0.08f,
                "Parmezán", 0.02f
        ));

        createRecipe("Salame", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.11f,
                "Salám", 0.06f
        ));

        createRecipe("Piccante", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.11f,
                "Salám", 0.05f,
                "Paprika", 0.05f,
                "Vejce", 0.05f,
                "Jalapeňos", 0.03f
        ));
        createRecipe("Mascarpone", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.11f,
                "Mascarpone", 0.05f,
                "Salám", 0.05f,
                "Jalapeňos", 0.03f
        ));

        createRecipe("Pancetta", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.11f,
                "Pancetta", 0.06f
        ));

        createRecipe("Spinaci Pancetta", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.11f,
                "Pancetta", 0.06f,
                "Špenát", 0.05f,
                "Vejce", 0.05f
        ));

        createRecipe("Gorgonzola", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.11f,
                "Pancetta", 0.06f,
                "Gorgonzola", 0.04f
        ));

        createRecipe("Mexicana", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.11f,
                "Pancetta", 0.06f,
                "Fazolky", 0.05f,
                "Červená cibule", 0.03f,
                "Jalapeňos", 0.03f
        ));

        createRecipe("Don Corleone", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.11f,
                "Šunka", 0.05f,
                "Salám", 0.04f,
                "Pancetta", 0.05f,
                "Žampiony", 0.07f
        ));

        createRecipe("Bestiale", Map.of(
                "Mouka", 0.25f,
                "Hořčice", 0.02f,
                "Mozzarella", 0.1f,
                "Uzený eidam", 0.05f,
                "Pancetta", 0.05f,
                "Salám", 0.04f,
                "Camembert", 0.04f,
                "Červená cibule", 0.03f,
                "Drcený pepř", 0.001f
        ));

        createRecipe("Papa Cipolla", Map.of(
                "Mouka", 0.25f,
                "Smetana", 0.05f,
                "Mozzarella", 0.1f,
                "Pancetta", 0.06f,
                "Červená cibule", 0.03f,
                "Pórek", 0.03f,
                "Parmská šunka", 0.05f,
                "Vejce", 0.05f
        ));

        createRecipe("Prosciutto Crudo", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.1f,
                "Parmská šunka", 0.05f,
                "Rukola", 0.015f
        ));

        createRecipe("Marco Polo", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.11f,
                "Grilovaná kuřecí prsa", 0.08f,
                "Rajčata", 0.05f,
                "Parmezán", 0.02f
        ));

        createRecipe("Broccoli Pollo", Map.of(
                "Mouka", 0.25f,
                "Smetana", 0.05f,
                "Mozzarella", 0.1f,
                "Brokolice", 0.07f,
                "Grilovaná kuřecí prsa", 0.08f,
                "Parmezán", 0.02f
        ));

        createRecipe("Pollo", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.11f,
                "Grilovaná kuřecí prsa", 0.08f
        ));

        createRecipe("Spinaci Pollo", Map.of(
                "Mouka", 0.25f,
                "Smetana", 0.05f,
                "Mozzarella", 0.1f,
                "Špenát", 0.05f,
                "Grilovaná kuřecí prsa", 0.08f,
                "Drcený pepř", 0.001f
        ));

        createRecipe("Siciliana Tonno", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.11f,
                "Tuňák", 0.07f,
                "Červená cibule", 0.03f
        ));

        createRecipe("Napoletana", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.1f,
                "Ančovičky", 0.03f,
                "Rajčata", 0.04f,
                "Olivy černé", 0.02f
        ));

        createRecipe("Carpaccio", Map.of(
                "Mouka", 0.25f,
                "Rajčatová omáčka", 0.08f,
                "Mozzarella", 0.1f,
                "Carpaccio z pravé svíčkové", 0.06f,
                "Rukola", 0.015f,
                "Parmezán", 0.02f,
                "Pesto", 0.01f
        ));

        createRecipe("Dolce Banana", Map.of(
                "Mouka", 0.25f,
                "Smetana", 0.05f,
                "Mascarpone", 0.05f,
                "Nutella", 0.05f,
                "Banán", 0.07f,
                "Vanilkový cukr", 0.005f
        ));

    }

    private void createRecipe(String pizzaName, Map<String, Float> ingredients) {
        Optional<Pizza> pizzaOpt = pizzaRepository.findAll().stream()
                .filter(p -> CustomUtilityString.normalize(p.getName()).equals(CustomUtilityString.normalize(pizzaName)))
                .findFirst();

        if (pizzaOpt.isEmpty()) return;
        Pizza pizza = pizzaOpt.get();

        for (Map.Entry<String, Float> entry : ingredients.entrySet()) {
            String ingName = entry.getKey();
            float quantity = entry.getValue();

            Ingredient ingredient = ingredientAliasService.findIngredientByNameFlexible(ingName)
                    .orElseThrow(() -> new IllegalStateException("Ingredient not found (even with aliases): " + ingName));

            boolean exists = recipeIngredientRepository
                    .findByPizzaId(pizza.getId()).stream()
                    .anyMatch(r -> r.getIngredient().getId().equals(ingredient.getId()));

            if (!exists) {
                RecipeIngredient ri = RecipeIngredient.builder()
                        .pizza(pizza)
                        .ingredient(ingredient)
                        .quantity(quantity)
                        .build();

                recipeIngredientRepository.save(ri);
            }
        }
    }
}