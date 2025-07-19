package leoric.pizzacipollastorage.init;

import jakarta.annotation.PostConstruct;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.utils.CustomUtilityString;
import leoric.pizzacipollastorage.vat.models.ProductCategory;
import leoric.pizzacipollastorage.vat.repositories.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@DependsOn({"vatRateInitializer", "productCategoryInitializer"})
public class IngredientInitializer {

    private final IngredientRepository ingredientRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @PostConstruct
    public void insertDefaultIngredients() {
//        ProductCategory defaultCategory = productCategoryRepository.findByNameIgnoreCase("FOOD")
//                .orElseThrow(() -> new EntityNotFoundException("Product category FOOD not found"));
//
//        createIfNotExists("Mozzarella", "g", 0.04f, 0.04f, defaultCategory);
//        createIfNotExists("Mouka", "g", 0.04f, 0.04f, defaultCategory);
//        createIfNotExists("Sugo", "g", 0.04f, 0.04f, defaultCategory);
//        createIfNotExists("Ančovičky", "g", 0.04f, 0.04f, defaultCategory);
//        createIfNotExists("Horcice", "g", 0.04f, 0.04f, defaultCategory);
//        createIfNotExists("Parmezán", "g", 0.09f, 0.07f, defaultCategory);
//        createIfNotExists("Jalapeňos", "g", 0.02f, 0.07f, defaultCategory);
//        createIfNotExists("Kukuřice", "g", 0.01f, 0.04f, defaultCategory);
//        createIfNotExists("Rukola", "g", 0.00f, 0.04f, defaultCategory);
//        createIfNotExists("Pesto alla genovese", "g", 0.03f, 0.06f, defaultCategory);
//        createIfNotExists("Červená cibule", "g", 0.03f, 0.03f, defaultCategory);
//        createIfNotExists("Česnek", "g", 0.01f, 0.06f, defaultCategory);
//        createIfNotExists("Pórek", "g", 0.02f, 0.05f, defaultCategory);
//        createIfNotExists("Brokolice", "g", 0.09f, 0.05f, defaultCategory);
//        createIfNotExists("Smetana", "l", 0.08f, 0.09f, defaultCategory);
//        createIfNotExists("Vejce", "ks", 0.06f, 0.06f, defaultCategory);
//        createIfNotExists("Fazolky", "g", 0.06f, 0.06f, defaultCategory);
//        createIfNotExists("Olivy černé", "g", 0.02f, 0.07f, defaultCategory);
//        createIfNotExists("Olivy zelené", "g", 0.06f, 0.07f, defaultCategory);
//        createIfNotExists("Olivy mix", "g", 0.01f, 0.05f, defaultCategory);
//        createIfNotExists("Žampiony", "g", 0.0f, 0.07f, defaultCategory);
//        createIfNotExists("Rajčata", "g", 0.01f, 0.05f, defaultCategory);
//        createIfNotExists("Paprika", "g", 0.05f, 0.07f, defaultCategory);
//        createIfNotExists("Salám", "g", 0.09f, 0.01f, defaultCategory);
//        createIfNotExists("Pancetta", "g", 0.04f, 0.04f, defaultCategory);
//        createIfNotExists("Šunka", "g", 0.02f, 0.0f, defaultCategory);
//        createIfNotExists("Grilovaná kuřecí prsa", "g", 0.05f, 0.02f, defaultCategory);
//        createIfNotExists("Ananas", "g", 0.08f, 0.09f, defaultCategory);
//        createIfNotExists("Camembert", "g", 0.09f, 0.04f, defaultCategory);
//        createIfNotExists("Uzený eidam", "g", 0.0f, 0.05f, defaultCategory);
//        createIfNotExists("Mascarpone", "g", 0.04f, 0.08f, defaultCategory);
//        createIfNotExists("Gorgonzola", "g", 0.04f, 0.02f, defaultCategory);
//        createIfNotExists("Špenát", "g", 0.08f, 0.01f, defaultCategory);
//        createIfNotExists("Tuňák", "g", 0.04f, 0.1f, defaultCategory);
//        createIfNotExists("Plody moře", "g", 0.09f, 0.05f, defaultCategory);
//        createIfNotExists("Bazalka", "g", 0.08f, 0.04f, defaultCategory);
//        createIfNotExists("Chili", "g", 0.1f, 0.02f, defaultCategory);
//        createIfNotExists("Nutella", "g", 0.02f, 0.09f, defaultCategory);
//        createIfNotExists("Mozzarella di bufala", "ks", 0.08f, 0.02f, defaultCategory);
//        createIfNotExists("Parmská šunka", "g", 0.04f, 0.09f, defaultCategory);
//        createIfNotExists("Drcený pepř", "g", 0.04f, 0.09f, defaultCategory);
//        createIfNotExists("Banán", "ks", 0.04f, 0.09f, defaultCategory);
//        createIfNotExists("Vanilkový cukr", "ks", 0.0f, 0.0f, defaultCategory);

    }

    private void createIfNotExists(String name, String unit, float lossCleaning, float lossUsage, ProductCategory category) {
        boolean exists = ingredientRepository.findAll().stream()
                .map(i -> CustomUtilityString.normalize(i.getName()))
                .anyMatch(n -> n.equals(CustomUtilityString.normalize(name)));

        if (!exists) {
            Ingredient ingredient = Ingredient.builder()
                    .name(name)
                    .unit(unit)
                    .lossCleaningFactor(lossCleaning)
                    .lossUsageFactor(lossUsage)
                    .productCategory(category)
                    .build();
            ingredientRepository.save(ingredient);
        }
    }
}