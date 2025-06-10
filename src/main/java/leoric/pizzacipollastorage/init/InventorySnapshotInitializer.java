package leoric.pizzacipollastorage.init;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.InventorySnapshot;
import leoric.pizzacipollastorage.models.enums.IngredientForm;
import leoric.pizzacipollastorage.models.enums.SnapshotType;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.repositories.InventorySnapshotRepository;
import leoric.pizzacipollastorage.utils.CustomUtilityString;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@DependsOn("ingredientInitializer")
public class InventorySnapshotInitializer {

    private final IngredientRepository ingredientRepository;
    private final InventorySnapshotRepository snapshotRepository;

    private static final LocalDateTime SNAPSHOT_TIME = LocalDateTime.of(2025, 5, 31, 14, 0);

    @PostConstruct
    public void insertDefaultSnapshots() {
        create("Mozzarella");
        create("sugo");
        create("Horcice");
        create("Parmezán");
        create("Jalapeňos");
        create("Kukuřice");
        create("Pesto alla genovese");
        create("Červená cibule");
        create("Česnek");
        create("Pórek");
        create("Brokolice");
        create("Smetana");
        create("Vejce");
        create("Olivy černé");
        create("Olivy zelené");
        create("Olivy mix");
        create("Žampiony");
        create("Rajčata");
        create("Paprika");
        create("Salám");
        create("Pancetta");
        create("Šunka");
        create("Grilovaná kuřecí prsa");
        create("Ananas");
        create("Camembert");
        create("Uzený eidam");
        create("Mascarpone");
        create("Gorgonzola");
        create("Špenát");
        create("Tuňák");
        create("Plody moře");
        create("Bazalka");
        create("Chili");
        create("Nutella");
        create("Mozzarella di bufala");
        create("Parmská šunka");
    }

    private void create(String ingredientName) {
        Ingredient ingredient = ingredientRepository.findAll().stream()
                .filter(i -> CustomUtilityString.normalize(i.getName()).equals(CustomUtilityString.normalize(ingredientName)))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found: " + ingredientName));

        IngredientForm form = switch (ingredient.getUnit()) {
            case "ks" -> IngredientForm.RAW;
            default -> IngredientForm.PREPARED;
        };

        InventorySnapshot snapshot = InventorySnapshot.builder()
                .ingredient(ingredient)
                .timestamp(SNAPSHOT_TIME)
                .measuredQuantity(1000f)
                .expectedQuantity(1000f)
                .note("Výchozí inventura")
                .form(form)
                .type(SnapshotType.INVENTORY)
                .build();

        snapshotRepository.save(snapshot);
    }
}