package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import leoric.pizzacipollastorage.DTOs.MenuItem.*;
import leoric.pizzacipollastorage.handler.exceptions.MissingQuantityException;
import leoric.pizzacipollastorage.mapstruct.MenuItemMapper;
import leoric.pizzacipollastorage.mapstruct.RecipeIngredientMapper;
import leoric.pizzacipollastorage.models.DishSize;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.MenuItem;
import leoric.pizzacipollastorage.models.RecipeIngredient;
import leoric.pizzacipollastorage.repositories.DishSizeRepository;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.repositories.MenuItemRepository;
import leoric.pizzacipollastorage.repositories.RecipeIngredientRepository;
import leoric.pizzacipollastorage.services.interfaces.IngredientAliasService;
import leoric.pizzacipollastorage.services.interfaces.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final DishSizeRepository dishSizeRepository;
    private final RecipeIngredientMapper recipeIngredientMapper;

    private final IngredientAliasService ingredientAliasService;

    private final MenuItemMapper menuItemMapper;

    @Override
    public List<RecipeIngredientShortDto> addIngredientsToMenuItemBulk(RecipeCreateBulkDto dto) {
        MenuItem menuItem = menuItemRepository.findByName(dto.getMenuItem())
                .orElseThrow(() -> new EntityNotFoundException("MenuItem not found: " + dto.getMenuItem()));

        // Získání dishSize (z requestu nebo výchozí)
        DishSize dishSize;
        if (dto.getDishSizeId() != null) {
            dishSize = dishSizeRepository.findById(dto.getDishSizeId())
                    .orElseThrow(() -> new EntityNotFoundException("Dish size not found: ID = " + dto.getDishSizeId()));
        } else {
            dishSize = dishSizeRepository.findByDefaultSizeTrue()
                    .orElseThrow(() -> new IllegalStateException("No default dish size defined."));
        }

        float dishFactor = dishSize.isDefaultSize() ? 1.0f : dishSize.getFactor();
        UUID defaultDishSizeId = dishSizeRepository.findByDefaultSizeTrue()
                .orElseThrow(() -> new IllegalStateException("No default dish size defined."))
                .getId();

        List<RecipeIngredientShortDto> created = new ArrayList<>();

        for (RecipeIngredientBulkDto ing : dto.getIngredients()) {
            // Najít ingredienci podle názvu nebo aliasu
            Ingredient ingredient = ingredientAliasService.findIngredientByNameFlexible(ing.getIngredientName())
                    .orElseThrow(() -> new EntityNotFoundException("Ingredient or alias not found: " + ing.getIngredientName()));

            float quantity = 0;

            if (dishSize.isDefaultSize()) {
                if (ing.getQuantity() == null) {
                    throw new MissingQuantityException("Quantity is required for ingredient '" + ing.getIngredientName() + "' when using default dish size.");
                }
                quantity = ing.getQuantity();
            } else {
                quantity = getQuantity(menuItem, dishFactor, defaultDishSizeId, ingredient, ing.getQuantity(), ing.getIngredientName());
            }

            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setMenuItem(menuItem);
            recipeIngredient.setIngredient(ingredient);
            recipeIngredient.setQuantity(quantity);
            recipeIngredient.setDishSize(dishSize);

            created.add(recipeIngredientMapper.toShortDto(recipeIngredientRepository.save(recipeIngredient)));
        }

        return created;
    }

    @Override
    public MenuItemResponseDto getMenuItemById(UUID id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem not found with ID: " + id));
        return menuItemMapper.toDto(menuItem);
    }

    @Override
    @Transactional
    public MenuItemResponseDto createMenuItemWithOptionalIngredients(MenuItemWithIngredientsCreateDto dto) {
        MenuItem menuItem = new MenuItem();
        menuItem.setName(dto.getName());
        menuItem.setDescription(dto.getDescription());
        menuItem = menuItemRepository.save(menuItem);

        if (dto.getIngredients() == null || dto.getIngredients().isEmpty()) {
            return menuItemMapper.toDto(menuItem);
        }

        DishSize dishSize = (dto.getDishSizeId() != null)
                ? dishSizeRepository.findById(dto.getDishSizeId())
                .orElseThrow(() -> new EntityNotFoundException("Dish size not found"))
                : dishSizeRepository.findByDefaultSizeTrue()
                .orElseThrow(() -> new IllegalStateException("No default dish size defined"));

        float dishFactor = dishSize.isDefaultSize() ? 1.0f : dishSize.getFactor();
        UUID defaultDishSizeId = dishSizeRepository.findByDefaultSizeTrue()
                .orElseThrow(() -> new IllegalStateException("No default dish size defined"))
                .getId();
        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        for (MenuItemWithIngredientsCreateDto.RecipeIngredientSimpleDto ingDto : dto.getIngredients()) {
            Ingredient ingredient = ingredientAliasService.findIngredientByNameFlexible(ingDto.getIngredientName())
                    .orElseThrow(() -> new EntityNotFoundException("Ingredient or alias not found: " + ingDto.getIngredientName()));

            float quantity;
            if (dishSize.isDefaultSize()) {
                if (ingDto.getQuantity() == null) {
                    throw new MissingQuantityException("Missing quantity for: " + ingDto.getIngredientName());
                }
                quantity = ingDto.getQuantity();
            } else {
                quantity = getQuantity(menuItem, dishFactor, defaultDishSizeId, ingredient, ingDto.getQuantity(), ingDto.getIngredientName());
            }

            RecipeIngredient ri = new RecipeIngredient();
            ri.setMenuItem(menuItem);
            ri.setIngredient(ingredient);
            ri.setQuantity(quantity);
            ri.setDishSize(dishSize);

            recipeIngredientRepository.save(ri);
            recipeIngredients.add(ri);
        }
        menuItem.setRecipeIngredients(recipeIngredients);
        return menuItemMapper.toDto(menuItem);
    }

    private float getQuantity(MenuItem menuItem, float dishFactor, UUID defaultDishSizeId, Ingredient ingredient, Float providedQuantity, String ingredientName) {
        Optional<RecipeIngredient> base = recipeIngredientRepository
                .findByMenuItemIdAndIngredientIdAndDishSizeId(menuItem.getId(), ingredient.getId(), defaultDishSizeId);

        if (base.isPresent()) {
            return base.get().getQuantity() * dishFactor;
        } else if (providedQuantity != null) {
            return providedQuantity;
        } else {
            throw new MissingQuantityException("Missing quantity for ingredient '" + ingredientName + "' and no base recipe found.");
        }
    }

    public MenuItem createMenuItem(MenuItemCreateDto dto) {
        MenuItem menuItem = new MenuItem();
        menuItem.setName(dto.getName());
        menuItem.setDescription(dto.getDescription());
        return menuItemRepository.save(menuItem);
    }

    public RecipeIngredientShortDto addIngredientToMenuItem(RecipeIngredientCreateDto dto) {
        MenuItem menuItem = menuItemRepository.findById(dto.getMenuItemId())
                .orElseThrow(() -> new EntityNotFoundException("MenuItem not found"));
        Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId())
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setMenuItem(menuItem);
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setQuantity(dto.getQuantity());

        return recipeIngredientMapper.toShortDto(recipeIngredientRepository.save(recipeIngredient));
    }

    @Override
    public List<MenuItemResponseDto> getAllMenuItems() {
        return menuItemMapper.toDtoList(menuItemRepository.findAll());
    }

    @Override
    public MenuItemResponseDto getMenuItemByName(String menuItemName) {
        MenuItem menuItem = menuItemRepository.findByName(menuItemName)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem " + menuItemName + "not found"));
        return menuItemMapper.toDto(menuItem);
    }
}