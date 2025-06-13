package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import leoric.pizzacipollastorage.DTOs.MenuItem.*;
import leoric.pizzacipollastorage.handler.exceptions.MissingQuantityException;
import leoric.pizzacipollastorage.mapstruct.MenuItemMapper;
import leoric.pizzacipollastorage.mapstruct.RecipeIngredientMapper;
import leoric.pizzacipollastorage.models.*;
import leoric.pizzacipollastorage.repositories.*;
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
    private final MenuItemCategoryRepository menuItemCategoryRepository;

    private final IngredientAliasService ingredientAliasService;

    private final MenuItemMapper menuItemMapper;

    @Override
    public void deleteMenuItemById(UUID id) {
        if (!menuItemRepository.existsById(id)) {
            throw new EntityNotFoundException("MenuItem not found: " + id);
        }
        menuItemRepository.deleteById(id);
    }

    @Override
    @Transactional
    public MenuItemResponseDto updateMenuItem(UUID id, MenuItemFullCreateDto dto) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem not found: " + id));

        menuItem.setName(dto.getName());
        menuItem.setDescription(dto.getDescription());
        menuItemRepository.save(menuItem);

        // clear existing recipeIngredients
        recipeIngredientRepository.deleteAllByMenuItemId(menuItem.getId());

        if (dto.getIngredients() != null && !dto.getIngredients().isEmpty()) {
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

            for (MenuItemFullCreateDto.RecipeIngredientSimpleDto ingDto : dto.getIngredients()) {
                Ingredient ingredient = ingredientRepository.findById(ingDto.getIngredientId())
                        .orElseThrow(() -> new EntityNotFoundException("Ingredient not found: " + ingDto.getIngredientId()));

                float quantity = (dishSize.isDefaultSize())
                        ? Optional.ofNullable(ingDto.getQuantity())
                        .orElseThrow(() -> new MissingQuantityException("Missing quantity for ingredient ID: " + ingDto.getIngredientId()))
                        : getQuantity(menuItem, dishFactor, defaultDishSizeId, ingredient, ingDto.getQuantity(), ingDto.getIngredientId().toString());

                RecipeIngredient ri = new RecipeIngredient();
                ri.setMenuItem(menuItem);
                ri.setIngredient(ingredient);
                ri.setQuantity(quantity);
                ri.setDishSize(dishSize);

                recipeIngredients.add(recipeIngredientRepository.save(ri));
            }

            menuItem.setRecipeIngredients(recipeIngredients);
        }

        return menuItemMapper.toDto(menuItem);
    }

    @Override
    @Transactional
    public RecipeIngredientShortDto updateRecipeIngredient(UUID recipeIngredientId, RecipeIngredientVeryShortDto dto) {
        RecipeIngredient recipeIngredient = recipeIngredientRepository.findById(recipeIngredientId)
                .orElseThrow(() -> new EntityNotFoundException("RecipeIngredient not found: " + recipeIngredientId));

        recipeIngredient.setQuantity(dto.getQuantity());

        RecipeIngredient updated = recipeIngredientRepository.save(recipeIngredient);

        return recipeIngredientMapper.toShortDto(updated);
    }

    @Override
    public RecipeIngredientShortDto getRecipeIngredientById(UUID id) {
        RecipeIngredient recipeIngredient = recipeIngredientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RecipeIngredient not found: " + id));
        return recipeIngredientMapper.toShortDto(recipeIngredient);
    }

    @Override
    public void deleteRecipeIngredientById(UUID id) {
        if (!recipeIngredientRepository.existsById(id)) {
            throw new EntityNotFoundException("RecipeIngredient not found: " + id);
        }
        recipeIngredientRepository.deleteById(id);
    }

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

            float quantity;

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
    public MenuItemResponseDto createMenuItemWithOptionalIngredients(MenuItemFullCreateDto dto) {
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
        for (MenuItemFullCreateDto.RecipeIngredientSimpleDto ingDto : dto.getIngredients()) {
            Ingredient ingredient = ingredientRepository.findById(ingDto.getIngredientId())
                    .orElseThrow(() -> new EntityNotFoundException("Ingredient not found: " + ingDto.getIngredientId()));

            float quantity;
            if (dishSize.isDefaultSize()) {
                if (ingDto.getQuantity() == null) {
                    throw new MissingQuantityException("Missing quantity for ingredient ID: " + ingDto.getIngredientId());
                }
                quantity = ingDto.getQuantity();
            } else {
                quantity = getQuantity(menuItem, dishFactor, defaultDishSizeId, ingredient, ingDto.getQuantity(), ingDto.getIngredientId().toString());
            }

            RecipeIngredient ri = new RecipeIngredient();
            ri.setMenuItem(menuItem);
            ri.setIngredient(ingredient);
            ri.setQuantity(quantity);
            ri.setDishSize(dishSize);

            recipeIngredientRepository.save(ri);
            recipeIngredients.add(ri);
        }
        if (dto.getMenuItemCategory() != null && dto.getMenuItemCategory().getName() != null) {
            String categoryName = dto.getMenuItemCategory().getName().trim();

            MenuItemCategory category = menuItemCategoryRepository.findByNameIgnoreCase(categoryName)
                    .orElseGet(() -> {
                        MenuItemCategory newCategory = new MenuItemCategory();
                        newCategory.setName(categoryName);
                        return menuItemCategoryRepository.save(newCategory);
                    });

            menuItem.setCategory(category);
        }
        menuItem.setRecipeIngredients(recipeIngredients);
        return menuItemMapper.toDto(menuItem);
    }

    private float getQuantity(MenuItem menuItem, float dishFactor, UUID defaultDishSizeId,
                              Ingredient ingredient, Float providedQuantity, String ingredientIdForError) {
        Optional<RecipeIngredient> base = recipeIngredientRepository
                .findByMenuItemIdAndIngredientIdAndDishSizeId(menuItem.getId(), ingredient.getId(), defaultDishSizeId);

        if (base.isPresent()) {
            return base.get().getQuantity() * dishFactor;
        } else if (providedQuantity != null) {
            return providedQuantity;
        } else {
            throw new MissingQuantityException("Missing quantity for ingredient ID '" + ingredientIdForError + "' and no base recipe found.");
        }
    }

    @Override
    @Transactional
    public MenuItemResponseDto createMenuItem(MenuItemCreateDto dto) {
        MenuItem menuItem = menuItemMapper.toEntity(dto);
        MenuItem saved = menuItemRepository.save(menuItem);
        return menuItemMapper.toDto(saved);
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