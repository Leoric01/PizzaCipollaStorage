package leoric.pizzacipollastorage.init;

import jakarta.annotation.PostConstruct;
import leoric.pizzacipollastorage.models.DishSize;
import leoric.pizzacipollastorage.repositories.DishSizeRepository;
import leoric.pizzacipollastorage.utils.CustomUtilityString;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DishSizeInitializer {

    private final DishSizeRepository dishSizeRepository;

    @PostConstruct
    public void insertDefaultDishSizes() {
        createIfNotExists("mala/base", 1.0f);
        createIfNotExists("Velká", 1.7f);
    }

    private void createIfNotExists(String name, float factor) {
        String normalizedInput = CustomUtilityString.normalize(name);

        boolean exists = dishSizeRepository.findAll().stream()
                .map(DishSize::getName)
                .map(CustomUtilityString::normalize)
                .anyMatch(existing -> existing.equals(normalizedInput));

        if (!exists) {
            DishSize dishSize = DishSize.builder()
                    .name(name)
                    .factor(factor)
                    .build();
            dishSizeRepository.save(dishSize);
        }
    }
}