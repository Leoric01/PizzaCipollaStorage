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
        createRecipe("Margherita", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 91f,
                "Bazalka", 5f
        ));

        createRecipe("Quattro Formaggi", Map.of(
                "Mouka", 250f,
                "Smetana", 50f,
                "Mozzarella", 80f,
                "Mascarpone", 50f,
                "Gorgonzola", 50f,
                "Parmezán", 20f
        ));

        createRecipe("Margherita Bufala", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella di bufala", 110f,
                "Bazalka", 5f
        ));

        createRecipe("Ai Formaggi", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 80f,
                "Uzený eidam", 50f,
                "Gorgonzola", 50f
        ));

        createRecipe("Funghi", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 110f,
                "Žampiony", 90f
        ));

        createRecipe("Santoška", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Uzený eidam", 50f,
                "Camembert", 50f,
                "Rajčata", 60f,
                "Olivy zelené", 30f
        ));

        createRecipe("Spinaci", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 110f,
                "Špenát", 60f,
                "Vejce", 50f
        ));

        createRecipe("Bandiera", Map.of(
                "Mouka", 250f,
                "Smetana", 50f,
                "Mozzarella", 110f,
                "Rajčata", 60f,
                "Rukola", 15f,
                "Česnek", 5f
        ));

        createRecipe("Vesuvio", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 110f,
                "Šunka", 100f
        ));

        createRecipe("Capricciosa", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 110f,
                "Šunka", 80f,
                "Žampiony", 90f
        ));

        createRecipe("Hawaii", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 110f,
                "Šunka", 80f,
                "Ananas", 70f
        ));

        createRecipe("Al Capone", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 110f,
                "Šunka", 60f,
                "Salám", 50f,
                "Paprika", 50f
        ));

        createRecipe("Diavola", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 110f,
                "Šunka", 60f,
                "Žampiony", 80f,
                "Jalapeňos", 30f
        ));

        createRecipe("Alla Crema", Map.of(
                "Mouka", 250f,
                "Smetana", 50f,
                "Mozzarella", 110f,
                "Šunka", 80f,
                "Parmezán", 20f
        ));

        createRecipe("Salame", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 110f,
                "Salám", 60f
        ));

        createRecipe("Piccante", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 110f,
                "Salám", 50f,
                "Paprika", 50f,
                "Vejce", 50f,
                "Jalapeňos", 30f
        ));

        createRecipe("Mascarpone", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 110f,
                "Mascarpone", 50f,
                "Salám", 50f,
                "Jalapeňos", 30f
        ));

        createRecipe("Pancetta", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 110f,
                "Pancetta", 60f
        ));

        createRecipe("Spinaci Pancetta", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 110f,
                "Pancetta", 60f,
                "Špenát", 50f,
                "Vejce", 50f
        ));

        createRecipe("Gorgonzola", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 110f,
                "Pancetta", 60f,
                "Gorgonzola", 40f
        ));

        createRecipe("Mexicana", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 110f,
                "Pancetta", 60f,
                "Fazolky", 50f,
                "Červená cibule", 30f,
                "Jalapeňos", 30f
        ));

        createRecipe("Don Corleone", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 110f,
                "Šunka", 50f,
                "Salám", 40f,
                "Pancetta", 50f,
                "Žampiony", 70f
        ));

        createRecipe("Bestiale", Map.of(
                "Mouka", 250f,
                "Hořčice", 20f,
                "Mozzarella", 100f,
                "Uzený eidam", 50f,
                "Pancetta", 50f,
                "Salám", 40f,
                "Camembert", 40f,
                "Červená cibule", 30f,
                "Drcený pepř", 1f
        ));

        createRecipe("Papa Cipolla", Map.of(
                "Mouka", 250f,
                "Smetana", 50f,
                "Mozzarella", 100f,
                "Pancetta", 60f,
                "Červená cibule", 30f,
                "Pórek", 30f,
                "Parmská šunka", 50f,
                "Vejce", 50f
        ));

        createRecipe("Prosciutto Crudo", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 100f,
                "Parmská šunka", 50f,
                "Rukola", 15f
        ));

        createRecipe("Marco Polo", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 110f,
                "Grilovaná kuřecí prsa", 80f,
                "Rajčata", 50f,
                "Parmezán", 20f
        ));

        createRecipe("Broccoli Pollo", Map.of(
                "Mouka", 250f,
                "Smetana", 50f,
                "Mozzarella", 100f,
                "Brokolice", 70f,
                "Grilovaná kuřecí prsa", 80f,
                "Parmezán", 20f
        ));

        createRecipe("Pollo", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 110f,
                "Grilovaná kuřecí prsa", 80f
        ));

        createRecipe("Spinaci Pollo", Map.of(
                "Mouka", 250f,
                "Smetana", 50f,
                "Mozzarella", 100f,
                "Špenát", 50f,
                "Grilovaná kuřecí prsa", 80f,
                "Drcený pepř", 1f
        ));

        createRecipe("Siciliana Tonno", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 110f,
                "Tuňák", 70f,
                "Červená cibule", 30f
        ));

        createRecipe("Napoletana", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 100f,
                "Ančovičky", 30f,
                "Rajčata", 40f,
                "Olivy černé", 20f
        ));

        createRecipe("Carpaccio", Map.of(
                "Mouka", 250f,
                "sugo", 80f,
                "Mozzarella", 100f,
                "Carpaccio z pravé svíčkové", 60f,
                "Rukola", 15f,
                "Parmezán", 20f,
                "Pesto", 10f
        ));

        createRecipe("Dolce Banana", Map.of(
                "Mouka", 250f,
                "Smetana", 50f,
                "Mascarpone", 50f,
                "Nutella", 50f,
                "Banán", 70f,
                "Vanilkový cukr", 5f
        ));

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