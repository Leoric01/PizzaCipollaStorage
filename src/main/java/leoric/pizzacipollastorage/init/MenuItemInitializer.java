package leoric.pizzacipollastorage.init;

import jakarta.annotation.PostConstruct;
import leoric.pizzacipollastorage.models.MenuItem;
import leoric.pizzacipollastorage.models.MenuItemCategory;
import leoric.pizzacipollastorage.repositories.MenuItemCategoryRepository;
import leoric.pizzacipollastorage.repositories.MenuItemRepository;
import leoric.pizzacipollastorage.utils.CustomUtilityString;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@DependsOn({"menuItemCategoryInitializer"})
public class MenuItemInitializer {

    private final MenuItemRepository menuItemRepository;
    private final MenuItemCategoryRepository menuItemCategoryRepository;

    @PostConstruct
    public void insertFullMenuItem() {
//        createIfNotExists("Margherita", "Tomato, mozzarella, basil.", "PIZZA");
//        createIfNotExists("Quattro Formaggi", "Cream, mozzarella, mascarpone, gorgonzola, parmesan.", "PIZZA");
//        createIfNotExists("Margherita Bufala", "Tomato, buffalo mozzarella, basil.", "PIZZA");
//        createIfNotExists("Ai Formaggi", "Tomato, mozzarella, smoked cheese, gorgonzola.", "PIZZA");
//        createIfNotExists("Funghi", "Tomato, mozzarella, mushrooms.", "PIZZA");
//        createIfNotExists("Vegetariana Verdure", "Tomato, mozzarella + 3x vegetable extras included!", "PIZZA");
//        createIfNotExists("Santoška", "Tomato, smoked cheese, camembert, tomatoes, green olives.", "PIZZA");
//        createIfNotExists("Spinaci", "Tomato, mozzarella, spinach, egg.", "PIZZA");
//        createIfNotExists("Bandiera", "Cream, mozzarella, tomatoes, rocket, garlic.", "PIZZA");
//        createIfNotExists("Vesuvio", "Tomato, mozzarella, ham off the bone.", "PIZZA");
//        createIfNotExists("Capricciosa", "Tomato, mozzarella, ham off the bone, mushrooms.", "PIZZA");
//        createIfNotExists("Hawaii", "Tomato, mozzarella, ham off the bone, pineapple.", "PIZZA");
//        createIfNotExists("Al Capone", "Tomato, mozzarella, ham, spicy salami, bell pepper.", "PIZZA");
//        createIfNotExists("Diavola", "Tomato, mozzarella, ham, mushrooms, jalapeños.", "PIZZA");
//        createIfNotExists("Alla Crema", "Cream, mozzarella, ham, parmesan.", "PIZZA");
//        createIfNotExists("Salame", "Tomato, mozzarella, spicy Italian salami.", "PIZZA");
//        createIfNotExists("Piccante", "Tomato, mozzarella, spicy salami, bell pepper, egg, jalapeños.", "PIZZA");
//        createIfNotExists("Mascarpone", "Tomato, mozzarella, mascarpone, spicy salami, jalapeños.", "PIZZA");
//        createIfNotExists("Pancetta", "Tomato, mozzarella, pancetta + 2x extras included!", "PIZZA");
//        createIfNotExists("Spinaci Pancetta", "Tomato, mozzarella, pancetta, spinach, egg.", "PIZZA");
//        createIfNotExists("Gorgonzola", "Tomato, mozzarella, pancetta, gorgonzola.", "PIZZA");
//        createIfNotExists("Mexicana", "Tomato, mozzarella, pancetta, beans, red onion, jalapeños.", "PIZZA");
//        createIfNotExists("Don Corleone", "Tomato, mozzarella, ham, salami, pancetta, mushrooms.", "PIZZA");
//        createIfNotExists("Bestiale", "Mustard, mozzarella, smoked cheese, pancetta, salami, camembert, onion, black pepper.", "PIZZA");
//        createIfNotExists("Papa Cipolla", "Cream, mozzarella, pancetta, red onion, leek, prosciutto, egg.", "PIZZA");
//        createIfNotExists("Prosciutto Crudo", "Tomato, mozzarella, prosciutto, rocket.", "PIZZA");
//        createIfNotExists("Marco Polo", "Tomato, mozzarella, grilled chicken, tomatoes, parmesan.", "PIZZA");
//        createIfNotExists("Broccoli Pollo", "Cream, mozzarella, broccoli, grilled chicken, parmesan.", "PIZZA");
//        createIfNotExists("Pollo", "Tomato, mozzarella, grilled chicken + 2x extras included!", "PIZZA");
//        createIfNotExists("Spinaci Pollo", "Cream, mozzarella, spinach, grilled chicken, black pepper.", "PIZZA");
//        createIfNotExists("Siciliana Tonno", "Tomato, mozzarella, tuna, red onion.", "PIZZA");
//        createIfNotExists("Napoletana", "Tomato, mozzarella, anchovy fillets, tomatoes, black olives.", "PIZZA");
//        createIfNotExists("Frutti di Mare", "Tomato, mozzarella, marinated seafood.", "PIZZA");
//        createIfNotExists("Dolce Banana", "Cream, mascarpone, Nutella, banana, vanilla sugar.", "PIZZA");
    }

    private void createIfNotExists(String name, String description, String categoryName) {
        String normalized = CustomUtilityString.normalize(name);
        boolean exists = menuItemRepository.findAll().stream()
                .map(MenuItem::getName)
                .map(CustomUtilityString::normalize)
                .anyMatch(n -> n.equals(normalized));

        if (!exists) {
            MenuItemCategory category = menuItemCategoryRepository.findAll().stream()
                    .filter(c -> CustomUtilityString.normalize(c.getName()).equals(CustomUtilityString.normalize(categoryName)))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Category not found: " + categoryName));

            MenuItem menuItem = MenuItem.builder()
                    .name(name)
                    .description(description)
                    .category(category)
                    .build();

            menuItemRepository.save(menuItem);
        }
    }
}