package leoric.pizzacipollastorage.init;

import jakarta.annotation.PostConstruct;
import leoric.pizzacipollastorage.models.MenuItemCategory;
import leoric.pizzacipollastorage.repositories.MenuItemCategoryRepository;
import leoric.pizzacipollastorage.utils.CustomUtilityString;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuItemCategoryInitializer {

    private final MenuItemCategoryRepository menuItemCategoryRepository;

    @PostConstruct
    public void insertDefaultCategories() {
        createIfNotExists("PIZZA");
        createIfNotExists("PASTA");
        createIfNotExists("GNOCCHI");
        createIfNotExists("PATATE");
        createIfNotExists("DRINK");
        createIfNotExists("DESSERT");
        createIfNotExists("SAUCE");
        createIfNotExists("OTHER");
        createIfNotExists("ALCOHOL");
        createIfNotExists("STARTER");
        createIfNotExists("SIDE");
        createIfNotExists("SPECIAL");
        createIfNotExists("VEGAN");
    }

    private void createIfNotExists(String name) {
        String normalized = CustomUtilityString.normalize(name);

        boolean exists = menuItemCategoryRepository.findAll().stream()
                .map(MenuItemCategory::getName)
                .map(CustomUtilityString::normalize)
                .anyMatch(n -> n.equals(normalized));

        if (!exists) {
            MenuItemCategory category = MenuItemCategory.builder()
                    .name(name)
                    .build();
            menuItemCategoryRepository.save(category);
        }
    }
}