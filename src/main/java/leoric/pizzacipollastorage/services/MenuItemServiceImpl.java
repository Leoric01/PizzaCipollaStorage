package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.MenuItem.*;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemMapNameResponseDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemNameWithSizesDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSizeDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.ThirdPartyNameMappingResponseDto;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.handler.BusinessErrorCodes;
import leoric.pizzacipollastorage.handler.exceptions.BusinessException;
import leoric.pizzacipollastorage.handler.exceptions.MissingQuantityException;
import leoric.pizzacipollastorage.handler.exceptions.NotAuthorizedForBranchException;
import leoric.pizzacipollastorage.mapstruct.MenuItemMapper;
import leoric.pizzacipollastorage.mapstruct.RecipeIngredientMapper;
import leoric.pizzacipollastorage.models.*;
import leoric.pizzacipollastorage.models.enums.DishSize;
import leoric.pizzacipollastorage.repositories.*;
import leoric.pizzacipollastorage.services.interfaces.IngredientAliasService;
import leoric.pizzacipollastorage.services.interfaces.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final BranchRepository branchRepository;
    private final MenuItemRepository menuItemRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeIngredientMapper recipeIngredientMapper;
    private final MenuItemCategoryRepository menuItemCategoryRepository;
    private final IgnoredThirdPartyNameRepository ignoredThirdPartyNameRepository;

    private final IngredientAliasService ingredientAliasService;

    private final MenuItemMapper menuItemMapper;

    @Override
    @Transactional
    public List<MenuItemResponseDto> duplicateMenuItemsDifferentDishSizes(
            UUID branchId,
            MenuItemDuplicateDifferentDishSizesRequestDto requestDto
    ) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        MenuItem original = menuItemRepository.findById(requestDto.menuItemId())
                .orElseThrow(() -> new EntityNotFoundException("MenuItem not found: " + requestDto.menuItemId()));

        if (!original.getBranch().getId().equals(branchId)) {
            throw new BusinessException(BusinessErrorCodes.NOT_AUTHORIZED_FOR_BRANCH);
        }

        List<MenuItem> duplicates = new ArrayList<>();

        for (DishSize size : requestDto.dishSizes()) {
            if (size.equals(original.getDishSize())) {
                continue;
            }

            MenuItem copy = new MenuItem();
            copy.setName(original.getName());
            copy.setDescription(original.getDescription());
            copy.setDishSize(size);
            copy.setBranch(branch);
            copy.setCategory(original.getCategory());

            List<RecipeIngredient> recipeIngredients = new ArrayList<>();
            for (RecipeIngredient originalRi : original.getRecipeIngredients()) {
                RecipeIngredient ri = new RecipeIngredient();
                ri.setMenuItem(copy);
                ri.setIngredient(originalRi.getIngredient());

                Float originalQuantity = originalRi.getQuantity();

                Float quantityAsM = normalizeToMedium(originalQuantity, original.getDishSize());

                Float adjustedQuantity = adjustQuantityBySize(quantityAsM, size);

                ri.setQuantity(adjustedQuantity);
                recipeIngredients.add(ri);
            }

            copy.setRecipeIngredients(recipeIngredients);
            duplicates.add(copy);
        }

        List<MenuItem> saved = menuItemRepository.saveAll(duplicates);
        return saved.stream()
                .map(menuItemMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ThirdPartyNameMappingResponseDto mapThirdPartyNames(UUID branchId, List<String> thirdPartyNames) {
        List<MenuItem> items = menuItemRepository.findByBranchIdAndThirdPartyNamesIn(branchId, thirdPartyNames);
        List<IgnoredThirdPartyName> ignored = ignoredThirdPartyNameRepository.findByBranchId(branchId);

        Set<String> ignoredNames = ignored.stream()
                .map(IgnoredThirdPartyName::getName)
                .collect(Collectors.toSet());

        Map<String, UUID> mapped = new HashMap<>();
        for (MenuItem item : items) {
            for (String tpName : item.getThirdPartyNames()) {
                if (thirdPartyNames.contains(tpName)) {
                    mapped.put(tpName, item.getId());
                }
            }
        }

        List<String> unmapped = thirdPartyNames.stream()
                .filter(name -> !mapped.containsKey(name) && !ignoredNames.contains(name))
                .toList();

        List<String> ignoredList = thirdPartyNames.stream()
                .filter(ignoredNames::contains)
                .toList();

        return new ThirdPartyNameMappingResponseDto(mapped, unmapped, ignoredList);
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

        menuItem.setRecipeIngredients(buildRecipeIngredients(dto.getIngredients(), menuItem, branchId));

        MenuItem saved = menuItemRepository.save(menuItem);
        return menuItemMapper.toDto(saved);
    }

    @Override
    @Transactional
    public List<MenuItemResponseDto> createMenuItemsBulk(UUID branchId, List<MenuItemFullCreateDto> dtos) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        List<MenuItem> menuItems = new ArrayList<>();

        for (MenuItemFullCreateDto dto : dtos) {
            MenuItem menuItem = new MenuItem();
            menuItem.setName(dto.getName());
            menuItem.setDescription(dto.getDescription());
            menuItem.setDishSize(dto.getSize());
            menuItem.setBranch(branch);

            MenuItemCategory category = null;
            if (dto.getMenuItemCategoryId() != null) {
                category = menuItemCategoryRepository.findById(dto.getMenuItemCategoryId())
                        .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            } else if (dto.getMenuItemCategory() != null && dto.getMenuItemCategory().getName() != null) {
                Optional<MenuItemCategory> existing = menuItemCategoryRepository
                        .findByNameIgnoreCaseAndBranchId(dto.getMenuItemCategory().getName().trim(), branchId);

                if (existing.isPresent()) {
                    category = existing.get();
                } else {
                    MenuItemCategory newCategory = new MenuItemCategory();
                    newCategory.setName(dto.getMenuItemCategory().getName().trim());
                    newCategory.setBranch(branch);
                    category = menuItemCategoryRepository.save(newCategory);
                }
            }

            menuItem.setCategory(category);
            menuItem.setRecipeIngredients(buildRecipeIngredients(dto.getIngredients(), menuItem, branchId));

            menuItems.add(menuItem);
        }

        List<MenuItem> saved = menuItemRepository.saveAll(menuItems);
        return menuItemMapper.toDtoList(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuItemResponseDto> menuItemGetAll(UUID branchId, String search, Pageable pageable) {
        Page<MenuItem> page;

        if (search != null && !search.isBlank()) {
            page = menuItemRepository.findByBranchIdAndNameContainingIgnoreCase(branchId, search, pageable);
        } else {
            page = menuItemRepository.findByBranchId(branchId, pageable);
        }

        return page.map(menuItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemResponseDto menuItemGetById(UUID branchId, UUID menuItemId) {
        MenuItem item = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new EntityNotFoundException("Menu item not found"));

        if (!item.getBranch().getId().equals(branchId)) {
            throw new BusinessException(BusinessErrorCodes.NOT_AUTHORIZED_FOR_BRANCH);
        }

        return menuItemMapper.toDto(item);
    }

    @Override
    @Transactional
    public MenuItemResponseDto menuItemUpdate(UUID branchId, UUID menuItemId, MenuItemFullCreateDto dto) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem not found: " + menuItemId));

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
    public MenuItemResponseDto menuItemGetByName(UUID branchId, String menuItemName) {
        MenuItem menuItem = menuItemRepository.findByNameIgnoreCaseAndBranchId(menuItemName, branchId)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem " + menuItemName + "not found"));
        return menuItemMapper.toDto(menuItem);
    }

    @Override
    public void menuItemDeleteById(UUID branchId, UUID menuItemId) {
        if (!menuItemRepository.existsById(menuItemId)) {
            throw new EntityNotFoundException("MenuItem not found: " + menuItemId);
        }
        menuItemRepository.deleteById(menuItemId);
    }

    @Override
    public RecipeIngredientShortDto getRecipeIngredientById(UUID branchId, UUID id) {
        RecipeIngredient recipeIngredient = recipeIngredientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RecipeIngredient not found: " + id));
        return recipeIngredientMapper.toShortDto(recipeIngredient);
    }

    @Override
    public void deleteRecipeIngredientById(UUID branchId, UUID id) {
        if (!recipeIngredientRepository.existsById(id)) {
            throw new EntityNotFoundException("RecipeIngredient not found: " + id);
        }
        recipeIngredientRepository.deleteById(id);
    }

    @Override
    public Page<MenuItemMapNameResponseDto> menuItemGetAllMapNames(UUID branchId, String search, Pageable pageable) {
        Page<MenuItem> page;

        if (search != null && !search.isBlank()) {
            page = menuItemRepository.findByBranchIdAndNameContainingIgnoreCase(branchId, search, pageable);
        } else {
            page = menuItemRepository.findByBranchId(branchId, pageable);
        }

        return page.map(menuItemMapper::toMapNameDto);
    }

    @Override
    public List<MenuItemNameWithSizesDto> getMenuItemNamesWithSizes(UUID branchId) {
        List<MenuItem> menuItems = menuItemRepository.findAllByBranchId(branchId);

        Map<String, List<MenuItemSizeDto>> grouped = menuItems.stream()
                .collect(Collectors.groupingBy(
                        MenuItem::getName,
                        Collectors.mapping(
                                mi -> new MenuItemSizeDto(mi.getId(), mi.getDishSize()),
                                Collectors.toList()
                        )
                ));

        return grouped.entrySet().stream()
                .map(e -> new MenuItemNameWithSizesDto(e.getKey(), e.getValue()))
                .toList();
    }

    @Override
    @Transactional
    public List<RecipeIngredientShortDto> recipeIngredientAddToMenuItemBulk(UUID branchId, RecipeCreateBulkDto dto) {
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

    @Override
    @Transactional
    public RecipeIngredientShortDto updateRecipeIngredient(UUID branchId, UUID id, RecipeIngredientVeryShortDto dto) {
        RecipeIngredient recipeIngredient = recipeIngredientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RecipeIngredient not found"));

        UUID menuItemBranchId = recipeIngredient.getMenuItem().getBranch().getId();
        if (!menuItemBranchId.equals(branchId)) {
            throw new NotAuthorizedForBranchException("You don't have access to this RecipeIngredient in this branch.");
        }

        UUID newIngredientId = dto.getIngredient().id();
        if (!recipeIngredient.getIngredient().getId().equals(newIngredientId)) {
            Ingredient newIngredient = ingredientRepository.findById(newIngredientId)
                    .orElseThrow(() -> new EntityNotFoundException("Ingredient not found: " + newIngredientId));
            recipeIngredient.setIngredient(newIngredient);
        }

        recipeIngredient.setQuantity(dto.getQuantity());

        RecipeIngredient saved = recipeIngredientRepository.save(recipeIngredient);
        return recipeIngredientMapper.toShortDto(saved);
    }

    @Override
    public RecipeIngredientShortDto recipeIngredientAddToMenuItem(UUID branchId, RecipeIngredientCreateDto dto) {
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
    @Transactional
    public void addThirdPartyName(UUID menuItemId, UUID branchId, String name) {
        MenuItem menuItem = menuItemRepository.findByIdAndBranchId(menuItemId, branchId)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem not found"));

        if (!menuItem.getThirdPartyNames().contains(name)) {
            menuItem.getThirdPartyNames().add(name);
            menuItemRepository.save(menuItem);
        }
    }

    @Override
    @Transactional
    public void updateThirdPartyName(UUID menuItemId, UUID branchId, String oldName, String newName) {
        MenuItem menuItem = menuItemRepository.findByIdAndBranchId(menuItemId, branchId)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem not found"));

        List<String> names = menuItem.getThirdPartyNames();
        int index = names.indexOf(oldName);
        if (index != -1) {
            names.set(index, newName);
            menuItemRepository.save(menuItem);
        } else {
            throw new EntityNotFoundException("Third party name not found: " + oldName);
        }
    }

    @Override
    @Transactional
    public void deleteThirdPartyName(UUID menuItemId, UUID branchId, String name) {
        MenuItem menuItem = menuItemRepository.findByIdAndBranchId(menuItemId, branchId)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem not found"));

        if (menuItem.getThirdPartyNames().remove(name)) {
            menuItemRepository.save(menuItem);
        } else {
            throw new EntityNotFoundException("Third party name not found: " + name);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponseDto> getMenuItemsByThirdPartyName(UUID branchId, String thirdPartyName) {
        List<MenuItem> menuItems = menuItemRepository.findAllByBranchIdAndThirdPartyName(branchId, thirdPartyName);

        if (menuItems.isEmpty()) {
            throw new EntityNotFoundException(
                    "No MenuItem found for third party name: " + thirdPartyName
            );
        }

        return menuItemMapper.toDtoList(menuItems);
    }

    private List<RecipeIngredient> buildRecipeIngredients(List<MenuItemFullCreateDto.RecipeIngredientSimpleDto> ingredients,
                                                          MenuItem menuItem,
                                                          UUID branchId) {
        if (ingredients == null || ingredients.isEmpty()) {
            return new ArrayList<>();
        }

        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        for (MenuItemFullCreateDto.RecipeIngredientSimpleDto ingDto : ingredients) {
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
        return recipeIngredients;
    }

    private float getScaleFactor(DishSize size) {
        return switch (size) {
            case XXS -> 0.25f;
            case XS -> 0.5f;
            case S -> 0.75f;
            case M -> 1.0f;
            case L -> 1.25f;
            case XL -> 1.5f;
            case XXL -> 1.75f;
            case XXXL -> 2.0f;
        };
    }

    // Přepočítá quantity z originálu na „Medium ekvivalent“
    private Float normalizeToMedium(Float quantity, DishSize originalSize) {
        float factor = getScaleFactor(originalSize);
        return quantity / factor; // převede třeba S množství na „M ekvivalent“
    }

    // Přepočítá z „Medium ekvivalentu“ na target size
    private Float adjustQuantityBySize(Float baseAsM, DishSize targetSize) {
        float factor = getScaleFactor(targetSize);
        return baseAsM * factor;
    }
}