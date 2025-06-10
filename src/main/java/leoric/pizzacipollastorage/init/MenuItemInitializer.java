package leoric.pizzacipollastorage.init;

import jakarta.annotation.PostConstruct;
import leoric.pizzacipollastorage.models.MenuItem;
import leoric.pizzacipollastorage.repositories.MenuItemRepository;
import leoric.pizzacipollastorage.utils.CustomUtilityString;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuItemInitializer {

    private final MenuItemRepository menuItemRepository;

    @PostConstruct
    public void insertFullMenuItem() {
        createIfNotExists("Margherita", "Tomato, mozzarella, basil.");
        createIfNotExists("Quattro Formaggi", "Cream, mozzarella, mascarpone, gorgonzola, parmesan.");
        createIfNotExists("Margherita Bufala", "Tomato, buffalo mozzarella, basil.");
        createIfNotExists("Ai Formaggi", "Tomato, mozzarella, smoked cheese, gorgonzola.");
        createIfNotExists("Funghi", "Tomato, mozzarella, mushrooms.");
        createIfNotExists("Vegetariana Verdure", "Tomato, mozzarella + 3x vegetable extras included!");
        createIfNotExists("Santoška", "Tomato, smoked cheese, camembert, tomatoes, green olives.");
        createIfNotExists("Spinaci", "Tomato, mozzarella, spinach, egg.");
        createIfNotExists("Bandiera", "Cream, mozzarella, tomatoes, rocket, garlic.");
        createIfNotExists("Vesuvio", "Tomato, mozzarella, ham off the bone.");
        createIfNotExists("Capricciosa", "Tomato, mozzarella, ham off the bone, mushrooms.");
        createIfNotExists("Hawaii", "Tomato, mozzarella, ham off the bone, pineapple.");
        createIfNotExists("Al Capone", "Tomato, mozzarella, ham, spicy salami, bell pepper.");
        createIfNotExists("Diavola", "Tomato, mozzarella, ham, mushrooms, jalapeños.");
        createIfNotExists("Alla Crema", "Cream, mozzarella, ham, parmesan.");
        createIfNotExists("Salame", "Tomato, mozzarella, spicy Italian salami.");
        createIfNotExists("Piccante", "Tomato, mozzarella, spicy salami, bell pepper, egg, jalapeños.");
        createIfNotExists("Mascarpone", "Tomato, mozzarella, mascarpone, spicy salami, jalapeños.");
        createIfNotExists("Pancetta", "Tomato, mozzarella, pancetta + 2x extras included!");
        createIfNotExists("Spinaci Pancetta", "Tomato, mozzarella, pancetta, spinach, egg.");
        createIfNotExists("Gorgonzola", "Tomato, mozzarella, pancetta, gorgonzola.");
        createIfNotExists("Mexicana", "Tomato, mozzarella, pancetta, beans, red onion, jalapeños.");
        createIfNotExists("Don Corleone", "Tomato, mozzarella, ham, salami, pancetta, mushrooms.");
        createIfNotExists("Bestiale", "Mustard, mozzarella, smoked cheese, pancetta, salami, camembert, onion, black pepper.");
        createIfNotExists("Papa Cipolla", "Cream, mozzarella, pancetta, red onion, leek, prosciutto, egg.");
        createIfNotExists("Prosciutto Crudo", "Tomato, mozzarella, prosciutto, rocket.");
        createIfNotExists("Marco Polo", "Tomato, mozzarella, grilled chicken, tomatoes, parmesan.");
        createIfNotExists("Broccoli Pollo", "Cream, mozzarella, broccoli, grilled chicken, parmesan.");
        createIfNotExists("Pollo", "Tomato, mozzarella, grilled chicken + 2x extras included!");
        createIfNotExists("Spinaci Pollo", "Cream, mozzarella, spinach, grilled chicken, black pepper.");
        createIfNotExists("Siciliana Tonno", "Tomato, mozzarella, tuna, red onion.");
        createIfNotExists("Napoletana", "Tomato, mozzarella, anchovy fillets, tomatoes, black olives.");
        createIfNotExists("Frutti di Mare", "Tomato, mozzarella, marinated seafood.");
        createIfNotExists("Dolce Banana", "Cream, mascarpone, Nutella, banana, vanilla sugar.");
    }

    private void createIfNotExists(String name, String description) {
        String normalized = CustomUtilityString.normalize(name);
        boolean exists = menuItemRepository.findAll().stream()
                .map(MenuItem::getName)
                .map(CustomUtilityString::normalize)
                .anyMatch(n -> n.equals(normalized));

        if (!exists) {
            MenuItem menuItem = MenuItem.builder()
                    .name(name)
                    .description(description)
                    .build();
            menuItemRepository.save(menuItem);
        }
    }
}