package leoric.pizzacipollastorage.init;

import jakarta.annotation.PostConstruct;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.VatRate;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.repositories.VatRateRepository;
import leoric.pizzacipollastorage.utils.CustomUtilityString;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@DependsOn("vatRateInitializer")
public class IngredientInitializer {

    private final IngredientRepository ingredientRepository;
    private final VatRateRepository vatRateRepository;

    @PostConstruct
    public void insertDefaultIngredients() {
        VatRate vatRate = vatRateRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("Missing VAT rate with ID 1"));

        createIfNotExists("Mozzarella", "g", 0.04f, 0.04f, vatRate);
        createIfNotExists("Mouka", "g", 0.04f, 0.04f, vatRate);
        createIfNotExists("Rajčatová omáčka", "g", 0.04f, 0.04f, vatRate);
        createIfNotExists("Ančovičky", "g", 0.04f, 0.04f, vatRate);
        createIfNotExists("Horcice", "g", 0.04f, 0.04f, vatRate);
        createIfNotExists("Parmezán", "g", 0.09f, 0.07f, vatRate);
        createIfNotExists("Jalapeňos", "g", 0.02f, 0.07f, vatRate);
        createIfNotExists("Kukuřice", "g", 0.01f, 0.04f, vatRate);
        createIfNotExists("Rukola", "g", 0.00f, 0.04f, vatRate);
        createIfNotExists("Pesto alla genovese", "g", 0.03f, 0.06f, vatRate);
        createIfNotExists("Červená cibule", "g", 0.03f, 0.03f, vatRate);
        createIfNotExists("Česnek", "g", 0.01f, 0.06f, vatRate);
        createIfNotExists("Pórek", "g", 0.02f, 0.05f, vatRate);
        createIfNotExists("Brokolice", "g", 0.09f, 0.05f, vatRate);
        createIfNotExists("Smetana", "g", 0.08f, 0.09f, vatRate);
        createIfNotExists("Vejce", "ks", 0.06f, 0.06f, vatRate);
        createIfNotExists("Fazolky", "ks", 0.06f, 0.06f, vatRate);
        createIfNotExists("Olivy černé", "g", 0.02f, 0.07f, vatRate);
        createIfNotExists("Olivy zelené", "g", 0.06f, 0.07f, vatRate);
        createIfNotExists("Olivy mix", "g", 0.01f, 0.05f, vatRate);
        createIfNotExists("Žampiony", "g", 0.0f, 0.07f, vatRate);
        createIfNotExists("Rajčata", "g", 0.01f, 0.05f, vatRate);
        createIfNotExists("Paprika", "g", 0.05f, 0.07f, vatRate);
        createIfNotExists("Salám", "g", 0.09f, 0.01f, vatRate);
        createIfNotExists("Pancetta", "g", 0.04f, 0.04f, vatRate);
        createIfNotExists("Šunka", "g", 0.02f, 0.0f, vatRate);
        createIfNotExists("Grilovaná kuřecí prsa", "g", 0.05f, 0.02f, vatRate);
        createIfNotExists("Ananas", "g", 0.08f, 0.09f, vatRate);
        createIfNotExists("Camembert", "g", 0.09f, 0.04f, vatRate);
        createIfNotExists("Uzený eidam", "g", 0.0f, 0.05f, vatRate);
        createIfNotExists("Mascarpone", "g", 0.04f, 0.08f, vatRate);
        createIfNotExists("Gorgonzola", "g", 0.04f, 0.02f, vatRate);
        createIfNotExists("Špenát", "g", 0.08f, 0.01f, vatRate);
        createIfNotExists("Tuňák", "g", 0.04f, 0.1f, vatRate);
        createIfNotExists("Plody moře", "g", 0.09f, 0.05f, vatRate);
        createIfNotExists("Bazalka", "g", 0.08f, 0.04f, vatRate);
        createIfNotExists("Chili", "g", 0.1f, 0.02f, vatRate);
        createIfNotExists("Nutella", "g", 0.02f, 0.09f, vatRate);
        createIfNotExists("Mozzarella di bufala", "ks", 0.08f, 0.02f, vatRate);
        createIfNotExists("Parmská šunka", "g", 0.04f, 0.09f, vatRate);
        createIfNotExists("Drcený pepř", "g", 0.04f, 0.09f, vatRate);
        createIfNotExists("Banán", "ks", 0.04f, 0.09f, vatRate);
        createIfNotExists("Vanilkový cukr", "ks", 0.0f, 0.0f, vatRate);

    }

    private void createIfNotExists(String name, String unit, float lossCleaning, float lossUsage, VatRate vatRate) {
        boolean exists = ingredientRepository.findAll().stream()
                .map(i -> CustomUtilityString.normalize(i.getName()))
                .anyMatch(n -> n.equals(CustomUtilityString.normalize(name)));

        if (!exists) {
            Ingredient ingredient = Ingredient.builder()
                    .name(name)
                    .unit(unit)
                    .lossCleaningFactor(lossCleaning)
                    .lossUsageFactor(lossUsage)
                    .vatRate(vatRate)
                    .build();
            ingredientRepository.save(ingredient);
        }
    }
}