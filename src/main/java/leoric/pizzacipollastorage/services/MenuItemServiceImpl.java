package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import leoric.pizzacipollastorage.DTOs.MenuItem.*;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.handler.BusinessErrorCodes;
import leoric.pizzacipollastorage.handler.exceptions.BusinessException;
import leoric.pizzacipollastorage.handler.exceptions.MissingQuantityException;
import leoric.pizzacipollastorage.mapstruct.MenuItemMapper;
import leoric.pizzacipollastorage.mapstruct.RecipeIngredientMapper;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.MenuItem;
import leoric.pizzacipollastorage.models.MenuItemCategory;
import leoric.pizzacipollastorage.models.RecipeIngredient;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.repositories.MenuItemCategoryRepository;
import leoric.pizzacipollastorage.repositories.MenuItemRepository;
import leoric.pizzacipollastorage.repositories.RecipeIngredientRepository;
import leoric.pizzacipollastorage.services.interfaces.IngredientAliasService;
import leoric.pizzacipollastorage.services.interfaces.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final BranchRepository branchRepository;
    private final MenuItemRepository menuItemRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
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
    public MenuItemResponseDto updateMenuItem(UUID branchId, UUID id, MenuItemFullCreateDto dto) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem not found: " + id));

        if (!menuItem.getBranch().getId().equals(branchId)) {
            throw new IllegalArgumentException("MenuItem does not belong to branch " + branchId);
        }

        menuItem.setName(dto.getName());
        menuItem.setDescription(dto.getDescription());
        menuItem.setDishSize(dto.getSize());

        if (dto.getMenuItemCategoryId() != null) {
            MenuItemCategory category = menuItemCategoryRepository.findById(dto.getMenuItemCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            menuItem.setCategory(category);
        }

        // Clear recipeIngredients
        menuItem.getRecipeIngredients().clear();

        if (dto.getIngredients() != null && !dto.getIngredients().isEmpty()) {
            for (MenuItemFullCreateDto.RecipeIngredientSimpleDto ingDto : dto.getIngredients()) {
                Ingredient ingredient = ingredientRepository.findById(ingDto.getIngredientId())
                        .orElseThrow(() -> new EntityNotFoundException("Ingredient not found: " + ingDto.getIngredientId()));

                if (ingDto.getQuantity() == null) {
                    throw new MissingQuantityException("Missing quantity for ingredient ID: " + ingDto.getIngredientId());
                }

                RecipeIngredient ri = new RecipeIngredient();
                ri.setMenuItem(menuItem);
                ri.setIngredient(ingredient);
                ri.setQuantity(ingDto.getQuantity());

                menuItem.getRecipeIngredients().add(ri);
            }
        }

        MenuItem saved = menuItemRepository.save(menuItem);
        return menuItemMapper.toDto(saved);
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
    @Transactional
    public List<RecipeIngredientShortDto> addIngredientsToMenuItemBulk(UUID branchId, RecipeCreateBulkDto dto) {
        MenuItem menuItem = menuItemRepository.findByNameIgnoreCaseAndBranchId(dto.getMenuItem(), branchId)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem not found: " + dto.getMenuItem()));

        List<RecipeIngredient> newIngredients = new ArrayList<>();

        for (RecipeIngredientBulkDto ing : dto.getIngredients()) {
            Ingredient ingredient = ingredientAliasService.findIngredientByNameFlexible(ing.getIngredientName())
                    .orElseThrow(() -> new EntityNotFoundException("Ingredient or alias not found: " + ing.getIngredientName()));

            if (ing.getQuantity() == null) {
                throw new MissingQuantityException("Quantity is required for ingredient '" + ing.getIngredientName() + "'.");
            }

            RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                    .menuItem(menuItem)
                    .ingredient(ingredient)
                    .quantity(ing.getQuantity())
                    .build();

            newIngredients.add(recipeIngredient);
        }

        List<RecipeIngredient> saved = recipeIngredientRepository.saveAll(newIngredients);
        return saved.stream()
                .map(recipeIngredientMapper::toShortDto)
                .toList();
    }

    public RecipeIngredientShortDto addIngredientToMenuItem(UUID branchId, RecipeIngredientCreateDto dto) {
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
    public MenuItemResponseDto getMenuItemById(UUID id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem not found with ID: " + id));
        return menuItemMapper.toDto(menuItem);
    }

    @Override
    @Transactional
    public MenuItemResponseDto createMenuItemWithOptionalIngredients(UUID branchId, MenuItemFullCreateDto dto) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        MenuItem menuItem = new MenuItem();
        menuItem.setName(dto.getName());
        menuItem.setDescription(dto.getDescription());
        menuItem.setDishSize(dto.getSize());
        menuItem.setBranch(branch);

        if (dto.getMenuItemCategoryId() != null) {
            MenuItemCategory category = menuItemCategoryRepository.findById(dto.getMenuItemCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));

            if (!category.getBranch().getId().equals(branchId)) {
                throw new BusinessException(BusinessErrorCodes.NOT_AUTHORIZED_FOR_BRANCH);
            }

            menuItem.setCategory(category);
        }

        List<RecipeIngredient> recipeIngredients = new ArrayList<>();

        for (MenuItemFullCreateDto.RecipeIngredientSimpleDto ingDto : dto.getIngredients()) {
            Ingredient ingredient = ingredientRepository.findById(ingDto.getIngredientId())
                    .orElseThrow(() -> new EntityNotFoundException("Ingredient not found: " + ingDto.getIngredientId()));

            if (!ingredient.getBranch().getId().equals(branchId)) {
                throw new BusinessException(BusinessErrorCodes.INGREDIENT_NOT_IN_BRANCH);
            }

            if (ingDto.getQuantity() == null) {
                throw new BusinessException(BusinessErrorCodes.MISSING_INGREDIENT_QUANTITY);
            }

            RecipeIngredient ri = new RecipeIngredient();
            ri.setMenuItem(menuItem);
            ri.setIngredient(ingredient);
            ri.setQuantity(ingDto.getQuantity());

            recipeIngredients.add(ri);
        }

        menuItem.setRecipeIngredients(recipeIngredients);

        MenuItem saved = menuItemRepository.save(menuItem);
        return menuItemMapper.toDto(saved);
    }

    @Override
    @Transactional
    public MenuItemResponseDto createMenuItem(MenuItemCreateDto dto) {
        MenuItem menuItem = menuItemMapper.toEntity(dto);
        MenuItem saved = menuItemRepository.save(menuItem);
        return menuItemMapper.toDto(saved);
    }

    @Override
    public List<MenuItemResponseDto> getAllMenuItems() {
        return menuItemMapper.toDtoList(menuItemRepository.findAll());
    }

    @Override
    public MenuItemResponseDto getMenuItemByName(UUID branchId, String menuItemName) {
        MenuItem menuItem = menuItemRepository.findByNameIgnoreCaseAndBranchId(menuItemName, branchId)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem " + menuItemName + "not found"));
        return menuItemMapper.toDto(menuItem);
    }
}