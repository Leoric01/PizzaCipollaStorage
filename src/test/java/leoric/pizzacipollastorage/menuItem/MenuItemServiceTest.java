package leoric.pizzacipollastorage.menuItem;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientShortDto;
import leoric.pizzacipollastorage.DTOs.MenuItem.*;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemMapNameResponseDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemNameWithSizesDto;
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
import leoric.pizzacipollastorage.services.MenuItemServiceImpl;
import leoric.pizzacipollastorage.services.interfaces.IngredientAliasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class MenuItemServiceTest {

    @Mock
    private IngredientAliasService ingredientAliasService;

    @Mock
    private MenuItemMapper menuItemMapper;

    @Mock
    private RecipeIngredientMapper recipeIngredientMapper;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private MenuItemCategoryRepository menuItemCategoryRepository;

    @Mock
    private RecipeIngredientRepository recipeIngredientRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private IgnoredThirdPartyNameRepository ignoredThirdPartyNameRepository;

    @Mock
    private Pageable pageable;

    @InjectMocks
    private MenuItemServiceImpl menuItemService;

    private UUID menuItemId;
    private UUID branchId;
    private UUID itemId1;
    private UUID itemId2;
    private MenuItem original;
    private Branch branch;
    private MenuItem menuItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        branchId = UUID.randomUUID();
        itemId1 = UUID.randomUUID();
        itemId2 = UUID.randomUUID();
        menuItemId = UUID.randomUUID();

        branch = Branch.builder()
                .id(branchId)
                .name("Test Branch")
                .build();
        original = new MenuItem();
        original.setId(menuItemId);
        original.setName("Vesuvio");
        original.setDescription("Delicious pizza");
        original.setDishSize(DishSize.M);
        original.setBranch(branch);
        original.setRecipeIngredients(new ArrayList<>());
        menuItem = MenuItem.builder()
                .id(menuItemId)
                .name("Vesuvio")
                .description("Delicious pizza")
                .dishSize(DishSize.M)
                .branch(branch)
                .recipeIngredients(new ArrayList<>())
                .build();
    }

    @Test
    void recipeIngredientAddToMenuItemBulk_shouldSaveAllIngredients() {
        UUID branchId = UUID.randomUUID();
        String menuItemName = "TestPizza";

        MenuItem menuItem = new MenuItem();
        menuItem.setId(UUID.randomUUID());
        menuItem.setName(menuItemName);

        RecipeIngredientBulkDto ingDto = new RecipeIngredientBulkDto();
        ingDto.setIngredientName("Cheese");
        ingDto.setQuantity(100f);

        RecipeCreateBulkDto dto = new RecipeCreateBulkDto();
        dto.setMenuItem(menuItemName);
        dto.setIngredients(List.of(ingDto));

        Ingredient ingredient = new Ingredient();
        ingredient.setId(UUID.randomUUID());

        RecipeIngredient savedIngredient = RecipeIngredient.builder()
                .menuItem(menuItem)
                .ingredient(ingredient)
                .quantity(100f)
                .build();

        RecipeIngredientShortDto shortDto = new RecipeIngredientShortDto();

        when(menuItemRepository.findByNameIgnoreCaseAndBranchId(menuItemName, branchId)).thenReturn(Optional.of(menuItem));
        when(ingredientAliasService.findIngredientByNameFlexible("Cheese")).thenReturn(Optional.of(ingredient));
        when(recipeIngredientRepository.saveAll(anyList())).thenReturn(List.of(savedIngredient));
        when(recipeIngredientMapper.toShortDto(savedIngredient)).thenReturn(shortDto);

        List<RecipeIngredientShortDto> result = menuItemService.recipeIngredientAddToMenuItemBulk(branchId, dto);

        assertEquals(1, result.size());
        assertSame(shortDto, result.get(0));
        verify(recipeIngredientRepository).saveAll(anyList());
    }

    @Test
    void recipeIngredientAddToMenuItemBulk_shouldThrowIfMenuItemNotFound() {
        UUID branchId = UUID.randomUUID();
        String menuItemName = "NotExist";

        RecipeCreateBulkDto dto = new RecipeCreateBulkDto();
        dto.setMenuItem(menuItemName);
        dto.setIngredients(List.of());

        when(menuItemRepository.findByNameIgnoreCaseAndBranchId(menuItemName, branchId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> menuItemService.recipeIngredientAddToMenuItemBulk(branchId, dto));
    }

    @Test
    void recipeIngredientAddToMenuItemBulk_shouldThrowIfIngredientNotFound() {
        UUID branchId = UUID.randomUUID();
        String menuItemName = "TestPizza";

        MenuItem menuItem = new MenuItem();
        menuItem.setId(UUID.randomUUID());

        RecipeIngredientBulkDto ingDto = new RecipeIngredientBulkDto();
        ingDto.setIngredientName("Unknown");
        ingDto.setQuantity(50f);

        RecipeCreateBulkDto dto = new RecipeCreateBulkDto();
        dto.setMenuItem(menuItemName);
        dto.setIngredients(List.of(ingDto));

        when(menuItemRepository.findByNameIgnoreCaseAndBranchId(menuItemName, branchId)).thenReturn(Optional.of(menuItem));
        when(ingredientAliasService.findIngredientByNameFlexible("Unknown")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> menuItemService.recipeIngredientAddToMenuItemBulk(branchId, dto));
    }

    @Test
    void createMenuItemsBulk_shouldUseExistingCategoryIfFound() {
        UUID branchId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        Branch branch = Branch.builder().id(branchId).build();

        MenuItemCategory existingCategory = new MenuItemCategory();
        existingCategory.setId(categoryId);
        existingCategory.setName("Pizza");
        existingCategory.setBranch(branch);

        MenuItemFullCreateDto dto = new MenuItemFullCreateDto();
        dto.setName("Margherita");
        dto.setDescription("Delicious pizza");
        dto.setSize(DishSize.M);
        dto.setMenuItemCategory(MenuItemCategoryCreateDto.builder().name("Pizza").build());
        dto.setIngredients(List.of());

        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
        when(menuItemCategoryRepository.findByNameIgnoreCaseAndBranchId("Pizza", branchId))
                .thenReturn(Optional.of(existingCategory));
        when(menuItemRepository.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));
        when(menuItemMapper.toDtoList(anyList())).thenAnswer(invocation -> {
            List<MenuItem> menuItems = invocation.getArgument(0);
            return menuItems.stream()
                    .map(mi -> MenuItemResponseDto.builder()
                            .id(mi.getId())
                            .name(mi.getName())
                            .description(mi.getDescription())
                            .dishSize(mi.getDishSize())
                            .menuItemCategory(mi.getCategory() == null ? null :
                                    new MenuItemCategoryResponseDto(mi.getCategory().getId(), mi.getCategory().getName()))
                            .ingredients(List.of())
                            .build())
                    .toList();
        });
        List<MenuItemResponseDto> result = menuItemService.createMenuItemsBulk(branchId, List.of(dto));
        MenuItemCategoryResponseDto categoryDto = result.get(0).getMenuItemCategory();
        assertNotNull(categoryDto);
        assertEquals(existingCategory.getId(), categoryDto.getId());
        assertEquals(existingCategory.getName(), categoryDto.getName());
        verify(menuItemCategoryRepository, never()).save(any());
    }

    @Test
    void recipeIngredientAddToMenuItemBulk_shouldThrowIfQuantityMissing() {
        UUID branchId = UUID.randomUUID();
        String menuItemName = "TestPizza";

        MenuItem menuItem = new MenuItem();
        menuItem.setId(UUID.randomUUID());

        RecipeIngredientBulkDto ingDto = new RecipeIngredientBulkDto();
        ingDto.setIngredientName("Cheese");
        ingDto.setQuantity(null);

        RecipeCreateBulkDto dto = new RecipeCreateBulkDto();
        dto.setMenuItem(menuItemName);
        dto.setIngredients(List.of(ingDto));

        Ingredient ingredient = new Ingredient();
        ingredient.setId(UUID.randomUUID());

        when(menuItemRepository.findByNameIgnoreCaseAndBranchId(menuItemName, branchId)).thenReturn(Optional.of(menuItem));
        when(ingredientAliasService.findIngredientByNameFlexible("Cheese")).thenReturn(Optional.of(ingredient));

        assertThrows(MissingQuantityException.class,
                () -> menuItemService.recipeIngredientAddToMenuItemBulk(branchId, dto));
    }

    @Test
    void menuItemGetAll_shouldReturnPageWithoutSearch() {
        UUID branchId = UUID.randomUUID();

        MenuItem menuItem = new MenuItem();
        menuItem.setId(UUID.randomUUID());
        menuItem.setName("Test Pizza");

        Page<MenuItem> page = new PageImpl<>(List.of(menuItem));
        when(menuItemRepository.findByBranchId(branchId, pageable)).thenReturn(page);

        MenuItemResponseDto dto = new MenuItemResponseDto();
        when(menuItemMapper.toDto(menuItem)).thenReturn(dto);

        Page<MenuItemResponseDto> result = menuItemService.menuItemGetAll(branchId, null, pageable);

        assertEquals(1, result.getContent().size());
        assertSame(dto, result.getContent().get(0));
        verify(menuItemRepository).findByBranchId(branchId, pageable);
    }

    @Test
    void menuItemGetAll_shouldReturnPageWithSearch() {
        UUID branchId = UUID.randomUUID();
        String search = "Vesuvio";

        MenuItem menuItem = new MenuItem();
        menuItem.setId(UUID.randomUUID());
        menuItem.setName(search);

        Page<MenuItem> page = new PageImpl<>(List.of(menuItem));
        when(menuItemRepository.findByBranchIdAndNameContainingIgnoreCase(branchId, search, pageable)).thenReturn(page);

        MenuItemResponseDto dto = new MenuItemResponseDto();
        when(menuItemMapper.toDto(menuItem)).thenReturn(dto);

        Page<MenuItemResponseDto> result = menuItemService.menuItemGetAll(branchId, search, pageable);

        assertEquals(1, result.getContent().size());
        assertSame(dto, result.getContent().get(0));
        verify(menuItemRepository).findByBranchIdAndNameContainingIgnoreCase(branchId, search, pageable);
    }

    @Test
    void menuItemGetAll_shouldReturnEmptyPage() {
        UUID branchId = UUID.randomUUID();
        Page<MenuItem> emptyPage = new PageImpl<>(List.of());
        when(menuItemRepository.findByBranchId(branchId, pageable)).thenReturn(emptyPage);

        Page<MenuItemResponseDto> result = menuItemService.menuItemGetAll(branchId, null, pageable);

        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void menuItemUpdate_shouldUpdateSuccessfully() {
        UUID branchId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID ingredientId = UUID.randomUUID();

        MenuItem menuItem = MenuItem.builder()
                .id(menuItemId)
                .branch(Branch.builder().id(branchId).build())
                .recipeIngredients(new ArrayList<>())
                .build();

        MenuItemCategory category = new MenuItemCategory();
        category.setId(categoryId);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientId);

        MenuItemFullCreateDto.RecipeIngredientSimpleDto ingDto = new MenuItemFullCreateDto.RecipeIngredientSimpleDto();
        ingDto.setIngredientId(ingredientId);
        ingDto.setQuantity(100f);

        MenuItemFullCreateDto dto = MenuItemFullCreateDto.builder()
                .name("Updated Pizza")
                .description("Updated Desc")
                .size(DishSize.L)
                .menuItemCategoryId(categoryId)
                .ingredients(List.of(ingDto))
                .build();

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));
        when(menuItemCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(menuItemRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(menuItemMapper.toDto(any())).thenReturn(new MenuItemResponseDto());

        MenuItemResponseDto result = menuItemService.menuItemUpdate(branchId, menuItemId, dto);

        assertNotNull(result);
        assertEquals("Updated Pizza", menuItem.getName());
        assertEquals("Updated Desc", menuItem.getDescription());
        assertEquals(DishSize.L, menuItem.getDishSize());
        assertEquals(category, menuItem.getCategory());
        assertEquals(1, menuItem.getRecipeIngredients().size());
        assertEquals(ingredient, menuItem.getRecipeIngredients().get(0).getIngredient());
        assertEquals(100f, menuItem.getRecipeIngredients().get(0).getQuantity());
    }

    @Test
    void menuItemUpdate_shouldThrowIfMenuItemNotFound() {
        UUID branchId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();

        MenuItemFullCreateDto dto = new MenuItemFullCreateDto();

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> menuItemService.menuItemUpdate(branchId, menuItemId, dto));
    }

    @Test
    void menuItemUpdate_shouldThrowIfMenuItemNotInBranch() {
        UUID branchId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();

        MenuItem menuItem = new MenuItem();
        menuItem.setBranch(Branch.builder().id(UUID.randomUUID()).build());

        MenuItemFullCreateDto dto = new MenuItemFullCreateDto();

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));

        assertThrows(IllegalArgumentException.class,
                () -> menuItemService.menuItemUpdate(branchId, menuItemId, dto));
    }

    @Test
    void menuItemUpdate_shouldThrowIfIngredientMissingQuantity() {
        UUID branchId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();
        UUID ingredientId = UUID.randomUUID();

        MenuItem menuItem = MenuItem.builder()
                .id(menuItemId)
                .branch(Branch.builder().id(branchId).build())
                .recipeIngredients(new ArrayList<>())
                .build();

        MenuItemFullCreateDto.RecipeIngredientSimpleDto ingDto = new MenuItemFullCreateDto.RecipeIngredientSimpleDto();
        ingDto.setIngredientId(ingredientId);
        ingDto.setQuantity(null);

        MenuItemFullCreateDto dto = MenuItemFullCreateDto.builder()
                .ingredients(List.of(ingDto))
                .build();

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));
        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(new Ingredient()));

        assertThrows(MissingQuantityException.class,
                () -> menuItemService.menuItemUpdate(branchId, menuItemId, dto));
    }

    @Test
    void menuItemGetByName_shouldReturnDtoIfExists() {
        UUID branchId = UUID.randomUUID();
        String name = "Pizza";
        MenuItem menuItem = new MenuItem();
        MenuItemResponseDto dto = new MenuItemResponseDto();

        when(menuItemRepository.findByNameIgnoreCaseAndBranchId(name, branchId))
                .thenReturn(Optional.of(menuItem));
        when(menuItemMapper.toDto(menuItem)).thenReturn(dto);

        MenuItemResponseDto result = menuItemService.menuItemGetByName(branchId, name);

        assertEquals(dto, result);
        verify(menuItemRepository).findByNameIgnoreCaseAndBranchId(name, branchId);
    }

    @Test
    void menuItemGetByName_shouldThrowIfNotExists() {
        UUID branchId = UUID.randomUUID();
        String name = "Pizza";

        when(menuItemRepository.findByNameIgnoreCaseAndBranchId(name, branchId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                menuItemService.menuItemGetByName(branchId, name)
        );
    }

    @Test
    void menuItemDeleteById_shouldDeleteIfExists() {
        UUID menuItemId = UUID.randomUUID();

        when(menuItemRepository.existsById(menuItemId)).thenReturn(true);

        menuItemService.menuItemDeleteById(UUID.randomUUID(), menuItemId);

        verify(menuItemRepository).deleteById(menuItemId);
    }

    @Test
    void menuItemDeleteById_shouldThrowIfNotExists() {
        UUID menuItemId = UUID.randomUUID();

        when(menuItemRepository.existsById(menuItemId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () ->
                menuItemService.menuItemDeleteById(UUID.randomUUID(), menuItemId)
        );

        verify(menuItemRepository, never()).deleteById(any());
    }

    @Test
    void deleteRecipeIngredientById_shouldDeleteIfExists() {
        UUID riId = UUID.randomUUID();

        when(recipeIngredientRepository.existsById(riId)).thenReturn(true);

        menuItemService.deleteRecipeIngredientById(UUID.randomUUID(), riId);

        verify(recipeIngredientRepository).deleteById(riId);
    }

    @Test
    void deleteRecipeIngredientById_shouldThrowIfNotExists() {
        UUID riId = UUID.randomUUID();

        when(recipeIngredientRepository.existsById(riId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () ->
                menuItemService.deleteRecipeIngredientById(UUID.randomUUID(), riId)
        );

        verify(recipeIngredientRepository, never()).deleteById(any());
    }

    @Test
    void getRecipeIngredientById_shouldReturnDtoIfExists() {
        UUID riId = UUID.randomUUID();
        RecipeIngredient ri = new RecipeIngredient();
        ri.setId(riId);
        RecipeIngredientShortDto dto = new RecipeIngredientShortDto();

        when(recipeIngredientRepository.findById(riId)).thenReturn(Optional.of(ri));
        when(recipeIngredientMapper.toShortDto(ri)).thenReturn(dto);

        RecipeIngredientShortDto result = menuItemService.getRecipeIngredientById(UUID.randomUUID(), riId);

        assertEquals(dto, result);
        verify(recipeIngredientRepository).findById(riId);
    }

    @Test
    void getRecipeIngredientById_shouldThrowIfNotExists() {
        UUID riId = UUID.randomUUID();

        when(recipeIngredientRepository.findById(riId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                menuItemService.getRecipeIngredientById(UUID.randomUUID(), riId)
        );

        verify(recipeIngredientRepository).findById(riId);
    }

    @Test
    void menuItemGetAllMapNames_shouldReturnMappedPage_whenSearchIsProvided() {
        UUID branchId = UUID.randomUUID();
        String search = "Pizza";
        Pageable pageable = PageRequest.of(0, 10);

        MenuItem item1 = MenuItem.builder()
                .id(UUID.randomUUID())
                .name("Vesuvio")
                .dishSize(DishSize.M)
                .thirdPartyNames(List.of("Vesuvio 32"))
                .build();

        MenuItem item2 = MenuItem.builder()
                .id(UUID.randomUUID())
                .name("Margherita")
                .dishSize(DishSize.L)
                .thirdPartyNames(List.of("Margarita 32"))
                .build();

        Page<MenuItem> page = new PageImpl<>(List.of(item1, item2), pageable, 2);

        when(menuItemRepository.findByBranchIdAndNameContainingIgnoreCase(branchId, search, pageable))
                .thenReturn(page);

        when(menuItemMapper.toMapNameDto(item1)).thenReturn(new MenuItemMapNameResponseDto(item1.getId(), item1.getName(), item1.getThirdPartyNames(), item1.getDishSize()));
        when(menuItemMapper.toMapNameDto(item2)).thenReturn(new MenuItemMapNameResponseDto(item2.getId(), item2.getName(), item2.getThirdPartyNames(), item2.getDishSize()));

        Page<MenuItemMapNameResponseDto> result = menuItemService.menuItemGetAllMapNames(branchId, search, pageable);

        assertEquals(2, result.getTotalElements());

        List<MenuItemMapNameResponseDto> content = result.getContent();
        assertEquals("Vesuvio", content.get(0).getName());
        assertEquals("Margherita", content.get(1).getName());

        verify(menuItemRepository).findByBranchIdAndNameContainingIgnoreCase(branchId, search, pageable);
    }

    @Test
    void menuItemGetAllMapNames_shouldCallFindByBranchId_whenSearchIsBlank() {
        UUID branchId = UUID.randomUUID();
        String search = "  ";
        Pageable pageable = PageRequest.of(0, 10);

        MenuItem item = MenuItem.builder()
                .id(UUID.randomUUID())
                .name("Vesuvio")
                .dishSize(DishSize.M)
                .build();

        Page<MenuItem> page = new PageImpl<>(List.of(item), pageable, 1);

        when(menuItemRepository.findByBranchId(branchId, pageable)).thenReturn(page);
        when(menuItemMapper.toMapNameDto(item)).thenReturn(new MenuItemMapNameResponseDto(item.getId(), item.getName(), item.getThirdPartyNames(), item.getDishSize()));

        Page<MenuItemMapNameResponseDto> result = menuItemService.menuItemGetAllMapNames(branchId, search, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Vesuvio", result.getContent().get(0).getName());

        verify(menuItemRepository).findByBranchId(branchId, pageable);
    }

    @Test
    void getMenuItemNamesWithSizes_shouldGroupByNameAndMapSizes() {
        UUID branchId = UUID.randomUUID();

        MenuItem item1 = MenuItem.builder()
                .id(UUID.randomUUID())
                .name("Vesuvio")
                .dishSize(DishSize.S)
                .branch(branch)
                .build();

        MenuItem item2 = MenuItem.builder()
                .id(UUID.randomUUID())
                .name("Vesuvio")
                .dishSize(DishSize.L)
                .branch(branch)
                .build();

        MenuItem item3 = MenuItem.builder()
                .id(UUID.randomUUID())
                .name("Margherita")
                .dishSize(DishSize.M)
                .branch(branch)
                .build();

        when(menuItemRepository.findAllByBranchId(branchId))
                .thenReturn(List.of(item1, item2, item3));

        List<MenuItemNameWithSizesDto> result = menuItemService.getMenuItemNamesWithSizes(branchId);

        assertEquals(2, result.size());

        MenuItemNameWithSizesDto vesuvioDto = result.stream()
                .filter(dto -> dto.getName().equals("Vesuvio"))
                .findFirst()
                .orElseThrow();

        assertEquals(2, vesuvioDto.getDishSizes().size());
        assertTrue(vesuvioDto.getDishSizes().stream()
                .anyMatch(d -> d.getSize() == DishSize.S));
        assertTrue(vesuvioDto.getDishSizes().stream()
                .anyMatch(d -> d.getSize() == DishSize.L));

        MenuItemNameWithSizesDto margheritaDto = result.stream()
                .filter(dto -> dto.getName().equals("Margherita"))
                .findFirst()
                .orElseThrow();

        assertEquals(1, margheritaDto.getDishSizes().size());
        assertEquals(DishSize.M, margheritaDto.getDishSizes().get(0).getSize());

        verify(menuItemRepository).findAllByBranchId(branchId);
    }

    @Test
    void updateRecipeIngredient_shouldUpdateQuantityOnly() {
        UUID branchId = UUID.randomUUID();
        UUID riId = UUID.randomUUID();
        UUID ingredientId = UUID.randomUUID();

        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientId);

        MenuItem menuItem = new MenuItem();
        menuItem.setBranch(new Branch());
        menuItem.getBranch().setId(branchId);

        RecipeIngredient ri = new RecipeIngredient();
        ri.setId(riId);
        ri.setIngredient(ingredient);
        ri.setMenuItem(menuItem);
        ri.setQuantity(50f);

        IngredientShortDto ingredientShortDto = new IngredientShortDto(ingredientId, "Test", "pcs");
        RecipeIngredientVeryShortDto dto = new RecipeIngredientVeryShortDto(ingredientShortDto, 100f);

        when(recipeIngredientRepository.findById(riId)).thenReturn(Optional.of(ri));
        when(recipeIngredientRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(recipeIngredientMapper.toShortDto(any())).thenReturn(new RecipeIngredientShortDto());

        RecipeIngredientShortDto result = menuItemService.updateRecipeIngredient(branchId, riId, dto);

        assertNotNull(result);
        assertEquals(100f, ri.getQuantity());
        verify(recipeIngredientRepository).save(ri);
    }

    @Test
    void updateRecipeIngredient_shouldUpdateIngredientAndQuantity() {
        UUID branchId = UUID.randomUUID();
        UUID riId = UUID.randomUUID();
        UUID oldIngredientId = UUID.randomUUID();
        UUID newIngredientId = UUID.randomUUID();

        Ingredient oldIngredient = new Ingredient();
        oldIngredient.setId(oldIngredientId);
        Ingredient newIngredient = new Ingredient();
        newIngredient.setId(newIngredientId);

        MenuItem menuItem = new MenuItem();
        menuItem.setBranch(new Branch());
        menuItem.getBranch().setId(branchId);

        RecipeIngredient ri = new RecipeIngredient();
        ri.setId(riId);
        ri.setIngredient(oldIngredient);
        ri.setMenuItem(menuItem);
        ri.setQuantity(50f);

        IngredientShortDto ingredientShortDto = new IngredientShortDto(newIngredientId, "Test", "pcs");
        RecipeIngredientVeryShortDto dto = new RecipeIngredientVeryShortDto(ingredientShortDto, 100f);

        when(recipeIngredientRepository.findById(riId)).thenReturn(Optional.of(ri));
        when(ingredientRepository.findById(newIngredientId)).thenReturn(Optional.of(newIngredient));
        when(recipeIngredientRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(recipeIngredientMapper.toShortDto(any())).thenReturn(new RecipeIngredientShortDto());

        RecipeIngredientShortDto result = menuItemService.updateRecipeIngredient(branchId, riId, dto);

        assertEquals(newIngredient, ri.getIngredient());
        assertEquals(100f, ri.getQuantity());
        verify(recipeIngredientRepository).save(ri);
    }

    @Test
    void updateRecipeIngredient_shouldThrowIfRecipeIngredientNotFound() {
        UUID riId = UUID.randomUUID();
        when(recipeIngredientRepository.findById(riId)).thenReturn(Optional.empty());
        IngredientShortDto ingredientShortDto = new IngredientShortDto(UUID.randomUUID(), "Test", "pcs");
        RecipeIngredientVeryShortDto dto = new RecipeIngredientVeryShortDto(ingredientShortDto, 100f);

        assertThrows(EntityNotFoundException.class, () ->
                menuItemService.updateRecipeIngredient(UUID.randomUUID(), riId, dto)
        );
    }

    @Test
    void updateRecipeIngredient_shouldThrowIfBranchMismatch() {
        UUID riId = UUID.randomUUID();
        Ingredient ingredient = new Ingredient();
        ingredient.setId(UUID.randomUUID());

        MenuItem menuItem = new MenuItem();
        menuItem.setBranch(new Branch());
        menuItem.getBranch().setId(UUID.randomUUID()); // jiná branch než volaná metoda

        RecipeIngredient ri = new RecipeIngredient();
        ri.setId(riId);
        ri.setIngredient(ingredient);
        ri.setMenuItem(menuItem);

        when(recipeIngredientRepository.findById(riId)).thenReturn(Optional.of(ri));

        IngredientShortDto ingredientShortDto = new IngredientShortDto(UUID.randomUUID(), "Test", "pcs");
        RecipeIngredientVeryShortDto dto = new RecipeIngredientVeryShortDto(ingredientShortDto, 100f);

        assertThrows(NotAuthorizedForBranchException.class, () ->
                menuItemService.updateRecipeIngredient(UUID.randomUUID(), riId, dto)
        );
    }

    @Test
    void updateRecipeIngredient_shouldThrowIfNewIngredientNotFound() {
        UUID branchId = UUID.randomUUID();
        UUID riId = UUID.randomUUID();
        UUID oldIngredientId = UUID.randomUUID();
        UUID newIngredientId = UUID.randomUUID();

        Ingredient oldIngredient = new Ingredient();
        oldIngredient.setId(oldIngredientId);

        MenuItem menuItem = new MenuItem();
        menuItem.setBranch(new Branch());
        menuItem.getBranch().setId(branchId);

        RecipeIngredient ri = new RecipeIngredient();
        ri.setId(riId);
        ri.setIngredient(oldIngredient);
        ri.setMenuItem(menuItem);

        IngredientShortDto ingredientShortDto = new IngredientShortDto(UUID.randomUUID(), "Test", "pcs");
        RecipeIngredientVeryShortDto dto = new RecipeIngredientVeryShortDto(ingredientShortDto, 100f);

        when(recipeIngredientRepository.findById(riId)).thenReturn(Optional.of(ri));
        when(ingredientRepository.findById(newIngredientId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                menuItemService.updateRecipeIngredient(branchId, riId, dto)
        );
    }

    @Test
    void recipeIngredientAddToMenuItem_shouldSaveAndReturnDto() {
        UUID branchId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();
        UUID ingredientId = UUID.randomUUID();

        MenuItem menuItem = new MenuItem();
        menuItem.setId(menuItemId);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientId);

        RecipeIngredientCreateDto dto = new RecipeIngredientCreateDto(menuItemId, ingredientId, 100f);

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));
        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(ingredient));

        RecipeIngredient saved = new RecipeIngredient();
        saved.setMenuItem(menuItem);
        saved.setIngredient(ingredient);
        saved.setQuantity(100f);

        when(recipeIngredientRepository.save(any())).thenReturn(saved);
        when(recipeIngredientMapper.toShortDto(saved)).thenReturn(RecipeIngredientShortDto.builder().build());

        RecipeIngredientShortDto result = menuItemService.recipeIngredientAddToMenuItem(branchId, dto);

        assertNotNull(result);
        verify(recipeIngredientRepository).save(any());
    }

    @Test
    void recipeIngredientAddToMenuItem_shouldThrowIfMenuItemNotFound() {
        UUID menuItemId = UUID.randomUUID();
        UUID ingredientId = UUID.randomUUID();
        RecipeIngredientCreateDto dto = new RecipeIngredientCreateDto(menuItemId, ingredientId, 100f);

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                menuItemService.recipeIngredientAddToMenuItem(UUID.randomUUID(), dto)
        );
    }

    @Test
    void recipeIngredientAddToMenuItem_shouldThrowIfIngredientNotFound() {
        UUID menuItemId = UUID.randomUUID();
        UUID ingredientId = UUID.randomUUID();

        MenuItem menuItem = new MenuItem();
        menuItem.setId(menuItemId);

        RecipeIngredientCreateDto dto = new RecipeIngredientCreateDto(menuItemId, ingredientId, 100f);

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));
        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                menuItemService.recipeIngredientAddToMenuItem(UUID.randomUUID(), dto)
        );
    }

    @Test
    void updateThirdPartyName_shouldUpdateExistingName() {
        UUID menuItemId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();
        String oldName = "Old";
        String newName = "New";

        MenuItem menuItem = new MenuItem();
        menuItem.setId(menuItemId);
        menuItem.setBranch(Branch.builder().id(branchId).name("Test").build());
        menuItem.setThirdPartyNames(new ArrayList<>(List.of(oldName)));

        when(menuItemRepository.findByIdAndBranchId(menuItemId, branchId))
                .thenReturn(Optional.of(menuItem));

        menuItemService.updateThirdPartyName(menuItemId, branchId, oldName, newName);

        assertTrue(menuItem.getThirdPartyNames().contains(newName));
        assertFalse(menuItem.getThirdPartyNames().contains(oldName));
        verify(menuItemRepository).save(menuItem);
    }

    @Test
    void updateThirdPartyName_shouldThrowIfOldNameNotFound() {
        UUID menuItemId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();

        MenuItem menuItem = new MenuItem();
        menuItem.setId(menuItemId);
        menuItem.setBranch(Branch.builder().id(branchId).name("Test").build());
        menuItem.setThirdPartyNames(new ArrayList<>(List.of("OtherName")));

        when(menuItemRepository.findByIdAndBranchId(menuItemId, branchId))
                .thenReturn(Optional.of(menuItem));

        assertThrows(EntityNotFoundException.class, () ->
                menuItemService.updateThirdPartyName(menuItemId, branchId, "OldName", "NewName")
        );
    }

    @Test
    void updateThirdPartyName_shouldThrowIfMenuItemNotFound() {
        UUID menuItemId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();

        when(menuItemRepository.findByIdAndBranchId(menuItemId, branchId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                menuItemService.updateThirdPartyName(menuItemId, branchId, "OldName", "NewName")
        );
    }

    @Test
    void addThirdPartyName_shouldAddNameIfNotPresent() {
        UUID menuItemId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();
        String name = "NewName";

        MenuItem menuItem = new MenuItem();
        menuItem.setId(menuItemId);
        menuItem.setBranch(Branch.builder().id(branchId).name("Test").build());
        menuItem.setThirdPartyNames(new ArrayList<>());

        when(menuItemRepository.findByIdAndBranchId(menuItemId, branchId))
                .thenReturn(Optional.of(menuItem));

        menuItemService.addThirdPartyName(menuItemId, branchId, name);

        assertTrue(menuItem.getThirdPartyNames().contains(name));
        verify(menuItemRepository).save(menuItem);
    }

    @Test
    void addThirdPartyName_shouldNotSaveIfNameAlreadyExists() {
        UUID menuItemId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();
        String name = "ExistingName";

        MenuItem menuItem = new MenuItem();
        menuItem.setId(menuItemId);
        menuItem.setBranch(Branch.builder().id(branchId).name("Test").build());
        menuItem.setThirdPartyNames(new ArrayList<>(List.of(name)));

        when(menuItemRepository.findByIdAndBranchId(menuItemId, branchId))
                .thenReturn(Optional.of(menuItem));

        menuItemService.addThirdPartyName(menuItemId, branchId, name);

        assertEquals(1, menuItem.getThirdPartyNames().size());
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    void addThirdPartyName_shouldThrowIfMenuItemNotFound() {
        UUID menuItemId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();
        String name = "NewName";

        when(menuItemRepository.findByIdAndBranchId(menuItemId, branchId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                menuItemService.addThirdPartyName(menuItemId, branchId, name)
        );
    }

    @Test
    void getMenuItemsByThirdPartyName_shouldReturnDtos() {
        UUID branchId = UUID.randomUUID();
        String thirdPartyName = "Vesuvio 32";

        MenuItem item = new MenuItem();
        item.setId(UUID.randomUUID());
        item.setName("Vesuvio");
        item.setBranch(Branch.builder().id(branchId).name("Test").build());

        when(menuItemRepository.findAllByBranchIdAndThirdPartyName(branchId, thirdPartyName))
                .thenReturn(List.of(item));
        when(menuItemMapper.toDtoList(anyList()))
                .thenReturn(List.of(new MenuItemResponseDto()));

        List<MenuItemResponseDto> result = menuItemService.getMenuItemsByThirdPartyName(branchId, thirdPartyName);

        assertEquals(1, result.size());
    }

    @Test
    void deleteThirdPartyName_shouldRemoveName() {
        UUID menuItemId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();
        String name = "Vesuvio 32";

        MenuItem menuItem = new MenuItem();
        menuItem.setId(menuItemId);
        menuItem.setBranch(Branch.builder().id(branchId).name("Test").build());
        menuItem.setThirdPartyNames(new ArrayList<>(List.of(name)));

        when(menuItemRepository.findByIdAndBranchId(menuItemId, branchId))
                .thenReturn(Optional.of(menuItem));

        menuItemService.deleteThirdPartyName(menuItemId, branchId, name);

        assertFalse(menuItem.getThirdPartyNames().contains(name));
        verify(menuItemRepository).save(menuItem);
    }

    @Test
    void deleteThirdPartyName_shouldThrow_whenMenuItemNotFound() {
        UUID menuItemId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();
        String name = "Vesuvio 32";

        when(menuItemRepository.findByIdAndBranchId(menuItemId, branchId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                menuItemService.deleteThirdPartyName(menuItemId, branchId, name)
        );
    }

    @Test
    void deleteThirdPartyName_shouldThrow_whenNameNotFound() {
        UUID menuItemId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();
        String name = "NonExisting";

        MenuItem menuItem = new MenuItem();
        menuItem.setId(menuItemId);
        menuItem.setBranch(Branch.builder().id(branchId).name("Test").build());
        menuItem.setThirdPartyNames(new ArrayList<>(List.of("Vesuvio 32")));

        when(menuItemRepository.findByIdAndBranchId(menuItemId, branchId))
                .thenReturn(Optional.of(menuItem));

        assertThrows(EntityNotFoundException.class, () ->
                menuItemService.deleteThirdPartyName(menuItemId, branchId, name)
        );
    }

    @Test
    void getMenuItemsByThirdPartyName_shouldThrowException_whenEmpty() {
        UUID branchId = UUID.randomUUID();
        String thirdPartyName = "NonExisting";

        when(menuItemRepository.findAllByBranchIdAndThirdPartyName(branchId, thirdPartyName))
                .thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () ->
                menuItemService.getMenuItemsByThirdPartyName(branchId, thirdPartyName)
        );
    }

    @Test
    void createMenuItemsBulk_shouldCreateWithExistingCategoryById() {
        UUID categoryId = UUID.randomUUID();
        MenuItemFullCreateDto dto = new MenuItemFullCreateDto();
        dto.setName("Vesuvio");
        dto.setSize(DishSize.M);
        dto.setMenuItemCategoryId(categoryId);
        dto.setIngredients(List.of());

        MenuItemCategory category = MenuItemCategory.builder().id(categoryId).branch(branch).build();

        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
        when(menuItemCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(menuItemRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));
        when(menuItemMapper.toDtoList(anyList())).thenAnswer(invocation ->
                ((List<MenuItem>) invocation.getArgument(0)).stream()
                        .map(m -> MenuItemResponseDto.builder().name(m.getName()).menuItemCategory(MenuItemCategoryResponseDto.builder().id(categoryId).build()).build())
                        .toList()
        );

        List<MenuItemResponseDto> result = menuItemService.createMenuItemsBulk(branchId, List.of(dto));

        assertEquals(1, result.size());
        assertEquals(categoryId, result.get(0).getMenuItemCategory().getId());
    }

    @Test
    void createMenuItemsBulk_shouldCreateNewCategoryWhenNameProvidedAndNotFound() {
        String categoryName = "Special";
        MenuItemFullCreateDto dto = new MenuItemFullCreateDto();
        dto.setName("Margherita");
        dto.setSize(DishSize.M);
        dto.setMenuItemCategory(MenuItemCategoryCreateDto.builder().name(categoryName).build());
        dto.setIngredients(List.of());

        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
        when(menuItemCategoryRepository.findByNameIgnoreCaseAndBranchId(categoryName, branchId)).thenReturn(Optional.empty());
        when(menuItemCategoryRepository.save(any(MenuItemCategory.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(menuItemRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));
        when(menuItemMapper.toDtoList(anyList())).thenAnswer(invocation ->
                ((List<MenuItem>) invocation.getArgument(0)).stream()
                        .map(m -> MenuItemResponseDto.builder().name(m.getName())
                                .menuItemCategory(MenuItemCategoryResponseDto.builder().name(categoryName).build()).build())
                        .toList()
        );

        List<MenuItemResponseDto> result = menuItemService.createMenuItemsBulk(branchId, List.of(dto));

        assertEquals(1, result.size());
        assertEquals(categoryName, result.get(0).getMenuItemCategory().getName());
    }

    @Test
    void createMenuItemsBulk_shouldThrow_whenCategoryIdNotFound() {
        UUID categoryId = UUID.randomUUID();
        MenuItemFullCreateDto dto = new MenuItemFullCreateDto();
        dto.setName("Vesuvio");
        dto.setSize(DishSize.M);
        dto.setMenuItemCategoryId(categoryId);

        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
        when(menuItemCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> menuItemService.createMenuItemsBulk(branchId, List.of(dto)));
        assertEquals("Category not found", ex.getMessage());
    }

    @Test
    void createMenuItemWithOptionalIngredients_shouldAssignCategorySuccessfully() {
        UUID categoryId = UUID.randomUUID();
        MenuItemCategory category = MenuItemCategory.builder()
                .id(categoryId)
                .branch(branch)
                .build();

        MenuItemFullCreateDto dto = new MenuItemFullCreateDto();
        dto.setName("Vesuvio");
        dto.setSize(DishSize.M);
        dto.setMenuItemCategoryId(categoryId);
        dto.setIngredients(List.of());

        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
        when(menuItemCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(menuItemRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(menuItemMapper.toDto(any())).thenAnswer(invocation -> {
            MenuItem m = invocation.getArgument(0);
            return MenuItemResponseDto.builder().name(m.getName()).menuItemCategory(MenuItemCategoryResponseDto.builder().id(categoryId).build()).build();
        });

        MenuItemResponseDto result = menuItemService.createMenuItemWithOptionalIngredients(branchId, dto);

        assertNotNull(result);
        assertEquals(categoryId, result.getMenuItemCategory().getId());
    }

    @Test
    void createMenuItemWithOptionalIngredients_shouldThrow_whenCategoryNotInBranch() {
        UUID categoryId = UUID.randomUUID();
        Branch otherBranch = Branch.builder().id(UUID.randomUUID()).build();
        MenuItemCategory category = MenuItemCategory.builder()
                .id(categoryId)
                .branch(otherBranch)
                .build();

        MenuItemFullCreateDto dto = new MenuItemFullCreateDto();
        dto.setName("Vesuvio");
        dto.setSize(DishSize.M);
        dto.setMenuItemCategoryId(categoryId);
        dto.setIngredients(List.of());

        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
        when(menuItemCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> menuItemService.createMenuItemWithOptionalIngredients(branchId, dto));
        assertEquals(BusinessErrorCodes.NOT_AUTHORIZED_FOR_BRANCH, ex.getErrorCode());
    }

    @Test
    void createMenuItemWithOptionalIngredients_shouldThrow_whenCategoryNotFound() {
        UUID categoryId = UUID.randomUUID();

        MenuItemFullCreateDto dto = new MenuItemFullCreateDto();
        dto.setName("Vesuvio");
        dto.setSize(DishSize.M);
        dto.setMenuItemCategoryId(categoryId);
        dto.setIngredients(List.of());

        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
        when(menuItemCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> menuItemService.createMenuItemWithOptionalIngredients(branchId, dto));
        assertEquals("Category not found", ex.getMessage());
    }

    @Test
    void createMenuItemWithOptionalIngredients_shouldThrow_whenBranchNotFound() {
        MenuItemFullCreateDto dto = new MenuItemFullCreateDto();
        dto.setIngredients(List.of());

        when(branchRepository.findById(branchId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> menuItemService.createMenuItemWithOptionalIngredients(branchId, dto));

        assertEquals("Branch not found", ex.getMessage());
    }

    @Test
    void createMenuItemWithOptionalIngredients_shouldThrow_whenIngredientNotInBranch() {
        UUID ingredientId = UUID.randomUUID();
        MenuItemFullCreateDto dto = new MenuItemFullCreateDto();
        dto.setIngredients(List.of(new MenuItemFullCreateDto.RecipeIngredientSimpleDto(ingredientId, 100f)));

        Branch otherBranch = Branch.builder().id(UUID.randomUUID()).build();

        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(Ingredient.builder().id(ingredientId).branch(otherBranch).build()));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> menuItemService.createMenuItemWithOptionalIngredients(branchId, dto));

        assertEquals(BusinessErrorCodes.INGREDIENT_NOT_IN_BRANCH, ex.getErrorCode());
    }

    @Test
    void createMenuItemWithOptionalIngredients_shouldThrow_whenQuantityNull() {
        UUID ingredientId = UUID.randomUUID();
        MenuItemFullCreateDto dto = new MenuItemFullCreateDto();
        dto.setIngredients(List.of(new MenuItemFullCreateDto.RecipeIngredientSimpleDto(ingredientId, null)));

        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(Ingredient.builder().id(ingredientId).branch(branch).build()));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> menuItemService.createMenuItemWithOptionalIngredients(branchId, dto));

        assertEquals(BusinessErrorCodes.MISSING_INGREDIENT_QUANTITY, ex.getErrorCode());
    }

    @Test
    void createMenuItemWithOptionalIngredients_shouldCreateSuccessfully() {
        MenuItemFullCreateDto dto = new MenuItemFullCreateDto();
        dto.setName("Vesuvio");
        dto.setDescription("Delicious pizza");
        dto.setSize(DishSize.M);
        dto.setMenuItemCategoryId(null);
        dto.setIngredients(List.of(
                new MenuItemFullCreateDto.RecipeIngredientSimpleDto(UUID.randomUUID(), 100f)
        ));

        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
        when(ingredientRepository.findById(any())).thenReturn(Optional.of(Ingredient.builder().id(UUID.randomUUID()).branch(branch).build()));
        when(menuItemRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(menuItemMapper.toDto(any())).thenAnswer(invocation -> MenuItemResponseDto.builder().name(dto.getName()).build());

        MenuItemResponseDto result = menuItemService.createMenuItemWithOptionalIngredients(branchId, dto);

        assertNotNull(result);
        assertEquals("Vesuvio", result.getName());
        verify(menuItemRepository).save(any());
    }

    @Test
    void menuItemGetById_shouldReturnDto_whenFoundAndAuthorized() {
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));
        MenuItemResponseDto dto = MenuItemResponseDto.builder()
                .id(menuItemId)
                .name(menuItem.getName())
                .dishSize(menuItem.getDishSize())
                .ingredients(new ArrayList<RecipeIngredientVeryShortDtoWithId>())
                .menuItemCategory(new MenuItemCategoryResponseDto(UUID.randomUUID(), "Pizza"))
                .build();
        when(menuItemMapper.toDto(menuItem)).thenReturn(dto);

        MenuItemResponseDto result = menuItemService.menuItemGetById(branchId, menuItemId);

        assertNotNull(result);
        assertEquals(menuItemId, result.getId());
        assertEquals(DishSize.M, result.getDishSize());
        verify(menuItemRepository).findById(menuItemId);
        verify(menuItemMapper).toDto(menuItem);
    }

    @Test
    void menuItemGetById_shouldThrowEntityNotFoundException_whenMenuItemDoesNotExist() {
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> menuItemService.menuItemGetById(branchId, menuItemId));

        assertEquals("Menu item not found", ex.getMessage());
    }

    @Test
    void menuItemGetById_shouldThrowBusinessException_whenNotAuthorizedForBranch() {
        Branch otherBranch = Branch.builder().id(UUID.randomUUID()).name("Other").build();
        menuItem.setBranch(otherBranch);

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> menuItemService.menuItemGetById(branchId, menuItemId));

        assertEquals(BusinessErrorCodes.NOT_AUTHORIZED_FOR_BRANCH, ex.getErrorCode());
    }

    @Test
    void duplicateMenuItemsDifferentDishSizes_shouldExcludeToCopyOriginalSize() {
        Ingredient ingredient = Ingredient.builder().id(UUID.randomUUID()).name("Tomato").build();
        RecipeIngredient ri = new RecipeIngredient();
        ri.setMenuItem(original);
        ri.setIngredient(ingredient);
        ri.setQuantity(100f);
        original.getRecipeIngredients().add(ri);

        List<DishSize> sizesToDuplicate = List.of(DishSize.XXS, DishSize.XS, DishSize.S, DishSize.M, DishSize.L, DishSize.XL, DishSize.XXL, DishSize.XXXL);
        MenuItemDuplicateDifferentDishSizesRequestDto requestDto =
                new MenuItemDuplicateDifferentDishSizesRequestDto(menuItemId, sizesToDuplicate);

        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(original));

        List<MenuItem> savedCopies = new ArrayList<>();
        for (DishSize size : sizesToDuplicate) {
            if (!size.equals(DishSize.M)) {
                MenuItem copy = new MenuItem();
                copy.setDishSize(size);
                savedCopies.add(copy);
            }
        }

        when(menuItemRepository.saveAll(anyList())).thenReturn(savedCopies);
        when(menuItemMapper.toDto(any())).thenAnswer(invocation -> {
            MenuItem m = invocation.getArgument(0);
            return MenuItemResponseDto.builder().dishSize(m.getDishSize()).build();
        });

        List<MenuItemResponseDto> result = menuItemService.duplicateMenuItemsDifferentDishSizes(branchId, requestDto);

        assertEquals(7, result.size());
        assertTrue(result.stream().anyMatch(dto -> dto.getDishSize() == DishSize.XXS));
        assertTrue(result.stream().anyMatch(dto -> dto.getDishSize() == DishSize.XS));
        assertTrue(result.stream().anyMatch(dto -> dto.getDishSize() == DishSize.S));
        assertFalse(result.stream().anyMatch(dto -> dto.getDishSize() == DishSize.M));
        assertTrue(result.stream().anyMatch(dto -> dto.getDishSize() == DishSize.L));
        assertTrue(result.stream().anyMatch(dto -> dto.getDishSize() == DishSize.XL));
        assertTrue(result.stream().anyMatch(dto -> dto.getDishSize() == DishSize.XXL));
        assertTrue(result.stream().anyMatch(dto -> dto.getDishSize() == DishSize.XXXL));

        ArgumentCaptor<List<MenuItem>> captor = ArgumentCaptor.forClass(List.class);
        verify(menuItemRepository).saveAll(captor.capture());

        List<MenuItem> captured = captor.getValue();
        assertEquals(7, captured.size());
        assertTrue(captured.stream().allMatch(mi -> mi.getDishSize() != DishSize.M));

        for (MenuItem copy : captured) {
            assertEquals(1, copy.getRecipeIngredients().size());
            assertEquals(ingredient, copy.getRecipeIngredients().get(0).getIngredient());
        }
    }

    @Test
    void mapThirdPartyNames_shouldMapCorrectly() {
        List<String> thirdPartyNames = List.of("Vesuvio 32", "Vesuvio 32cm", "Mhergharita", "Gellato");

        MenuItem item1 = new MenuItem();
        item1.setId(itemId1);
        item1.setThirdPartyNames(List.of("Vesuvio 32", "Vesuvio 32cm"));

        MenuItem item2 = new MenuItem();
        item2.setId(itemId2);
        item2.setThirdPartyNames(List.of("Mhergharita"));

        when(menuItemRepository.findByBranchIdAndThirdPartyNamesIn(branchId, thirdPartyNames))
                .thenReturn(List.of(item1, item2));

        IgnoredThirdPartyName ignored = new IgnoredThirdPartyName();
        ignored.setName("Gellato");

        when(ignoredThirdPartyNameRepository.findByBranchId(branchId))
                .thenReturn(List.of(ignored));

        ThirdPartyNameMappingResponseDto result = menuItemService.mapThirdPartyNames(branchId, thirdPartyNames);

        assertEquals(3, result.getMapped().size());
        assertEquals(itemId1, result.getMapped().get("Vesuvio 32"));
        assertEquals(itemId1, result.getMapped().get("Vesuvio 32cm"));
        assertEquals(itemId2, result.getMapped().get("Mhergharita"));

        assertTrue(result.getUnmapped().isEmpty());
        assertEquals(1, result.getIgnored().size());
        assertEquals("Gellato", result.getIgnored().get(0));
    }

    @Test
    void mapThirdPartyNames_shouldHandleAllUnmapped() {
        List<String> thirdPartyNames = List.of("NonExisting1", "NonExisting2");

        when(menuItemRepository.findByBranchIdAndThirdPartyNamesIn(branchId, thirdPartyNames))
                .thenReturn(List.of());

        when(ignoredThirdPartyNameRepository.findByBranchId(branchId))
                .thenReturn(List.of());

        ThirdPartyNameMappingResponseDto result = menuItemService.mapThirdPartyNames(branchId, thirdPartyNames);

        assertTrue(result.getMapped().isEmpty());
        assertEquals(2, result.getUnmapped().size());
        assertTrue(result.getIgnored().isEmpty());
    }

    @Test
    void mapThirdPartyNames_shouldHandleAllIgnored() {
        List<String> thirdPartyNames = List.of("Ignored1", "Ignored2");

        when(menuItemRepository.findByBranchIdAndThirdPartyNamesIn(branchId, thirdPartyNames))
                .thenReturn(List.of());

        IgnoredThirdPartyName ignored1 = new IgnoredThirdPartyName();
        ignored1.setName("Ignored1");
        IgnoredThirdPartyName ignored2 = new IgnoredThirdPartyName();
        ignored2.setName("Ignored2");

        when(ignoredThirdPartyNameRepository.findByBranchId(branchId))
                .thenReturn(List.of(ignored1, ignored2));

        ThirdPartyNameMappingResponseDto result = menuItemService.mapThirdPartyNames(branchId, thirdPartyNames);

        assertTrue(result.getMapped().isEmpty());
        assertTrue(result.getUnmapped().isEmpty());
        assertEquals(2, result.getIgnored().size());
        assertTrue(result.getIgnored().containsAll(thirdPartyNames));
    }

    @Test
    void duplicateMenuItems_shouldThrowException_whenBranchNotFound() {
        UUID unknownBranchId = UUID.randomUUID();
        MenuItemDuplicateDifferentDishSizesRequestDto requestDto =
                new MenuItemDuplicateDifferentDishSizesRequestDto(menuItemId, List.of(DishSize.S));

        when(branchRepository.findById(unknownBranchId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> menuItemService.duplicateMenuItemsDifferentDishSizes(unknownBranchId, requestDto));

        assertEquals("Branch not found", ex.getMessage());
    }

    @Test
    void duplicateMenuItems_shouldThrowException_whenMenuItemNotFound() {
        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
        MenuItemDuplicateDifferentDishSizesRequestDto requestDto =
                new MenuItemDuplicateDifferentDishSizesRequestDto(UUID.randomUUID(), List.of(DishSize.S));

        when(menuItemRepository.findById(requestDto.menuItemId())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> menuItemService.duplicateMenuItemsDifferentDishSizes(branchId, requestDto));

        assertEquals("MenuItem not found: " + requestDto.menuItemId(), ex.getMessage());
    }

    @Test
    void duplicateMenuItems_shouldThrowException_whenNotAuthorizedForBranch() {
        Branch otherBranch = Branch.builder().id(UUID.randomUUID()).name("Other").build();
        original.setBranch(otherBranch);
        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(original));

        MenuItemDuplicateDifferentDishSizesRequestDto requestDto =
                new MenuItemDuplicateDifferentDishSizesRequestDto(menuItemId, List.of(DishSize.S));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> menuItemService.duplicateMenuItemsDifferentDishSizes(branchId, requestDto));

        assertEquals(BusinessErrorCodes.NOT_AUTHORIZED_FOR_BRANCH, ex.getErrorCode());
    }
}