package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientAlias.IngredientAliasOverviewDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientCreateDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientResponseDto;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.handler.BusinessErrorCodes;
import leoric.pizzacipollastorage.handler.exceptions.BusinessException;
import leoric.pizzacipollastorage.handler.exceptions.DuplicateIngredientNameException;
import leoric.pizzacipollastorage.handler.exceptions.IngredientInUseException;
import leoric.pizzacipollastorage.inventory.models.InventorySnapshot;
import leoric.pizzacipollastorage.inventory.repositories.InventorySnapshotRepository;
import leoric.pizzacipollastorage.mapstruct.IngredientMapper;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.IngredientAlias;
import leoric.pizzacipollastorage.models.MenuItem;
import leoric.pizzacipollastorage.models.enums.InventoryStatus;
import leoric.pizzacipollastorage.models.enums.OrderItemStatus;
import leoric.pizzacipollastorage.models.enums.OrderStatus;
import leoric.pizzacipollastorage.purchase.models.PurchaseOrder;
import leoric.pizzacipollastorage.purchase.models.PurchaseOrderItem;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.repositories.MenuItemRepository;
import leoric.pizzacipollastorage.services.interfaces.IngredientAliasService;
import leoric.pizzacipollastorage.services.interfaces.IngredientService;
import leoric.pizzacipollastorage.vat.models.ProductCategory;
import leoric.pizzacipollastorage.vat.models.VatRate;
import leoric.pizzacipollastorage.vat.repositories.ProductCategoryRepository;
import leoric.pizzacipollastorage.vat.repositories.VatRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final BranchRepository branchRepository;
    private final IngredientRepository ingredientRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final InventorySnapshotRepository inventorySnapshotRepository;
    private final MenuItemRepository menuItemRepository;
    private final VatRateRepository vatRateRepository;

    private final IngredientMapper ingredientMapper;

    private final IngredientAliasService ingredientAliasService;

    @Override
    public IngredientResponseDto updateIngredient(UUID branchId, UUID id, IngredientCreateDto dto) {
        Ingredient existing = ingredientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found: " + id));

        if (!existing.getName().equalsIgnoreCase(dto.getName()) &&
            ingredientRepository.existsByNameIgnoreCaseAndBranchId(dto.getName(), branchId)) {
            throw new DuplicateIngredientNameException("Ingredient with name '" + dto.getName() + "' already exists");
        }

        ProductCategory category = productCategoryRepository
                .findByNameIgnoreCaseAndBranchId(dto.getProductCategory(), branchId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + dto.getProductCategory()));

        ingredientMapper.update(existing, dto);
        existing.setProductCategory(category);

        Ingredient saved = ingredientRepository.save(existing);
        return ingredientMapper.toDto(saved);
    }

    @Override
    public void deleteById(UUID branchId, UUID id) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ingredient with id " + id + " not found"));

        try {
            ingredientRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            List<MenuItem> menuItems = menuItemRepository.findAllByRecipeIngredientsIngredientId(id);
            String names = menuItems.stream()
                    .map(MenuItem::getName)
                    .collect(Collectors.joining(", "));

            throw new IngredientInUseException(ingredient.getId().toString(), ingredient.getName(), names);
        }
    }

    @Override
    public IngredientResponseDto createIngredient(UUID branchId, IngredientCreateDto dto) {
        String name = dto.getName().trim();

        if (ingredientRepository.existsByNameIgnoreCaseAndBranchId(name, branchId)) {
            throw new BusinessException(BusinessErrorCodes.DUPLICATE_INGREDIENT_NAME);
        }

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        UUID defaultVatRateId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        VatRate defaultVatRate = vatRateRepository.findById(defaultVatRateId)
                .orElseThrow(() -> new IllegalStateException("Default VAT rate not found"));

        String categoryName = dto.getProductCategory() == null || dto.getProductCategory().isBlank()
                ? "UNKNOWN"
                : dto.getProductCategory().trim().toUpperCase();

        ProductCategory category = productCategoryRepository
                .findByNameIgnoreCaseAndBranchId(categoryName, branchId)
                .orElseGet(() -> productCategoryRepository.save(
                        ProductCategory.builder()
                                .name(categoryName)
                                .vatRate(defaultVatRate)
                                .branch(branch)
                                .build()
                ));

        Ingredient ingredient = ingredientMapper.toEntity(dto);
        ingredient.setBranch(branch);
        ingredient.setProductCategory(category);

        Ingredient saved = ingredientRepository.save(ingredient);
        return ingredientMapper.toDto(saved);
    }

    @Override
    public List<IngredientResponseDto> getAllIngredients(UUID branchId) {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredientMapper.toDtoList(ingredients);
    }

    @Override
    public InventoryStatus checkInventoryStatus(Ingredient ingredient) {
        Optional<InventorySnapshot> latestSnapshotOpt =
                inventorySnapshotRepository.findTopByIngredientOrderByTimestampDesc(ingredient);

        if (latestSnapshotOpt.isEmpty()) return InventoryStatus.UNKNOWN;

        InventorySnapshot snapshot = latestSnapshotOpt.get();
        float measuredQuantity = snapshot.getMeasuredQuantity();

        if (ingredient.getMinimumStockLevel() != null && measuredQuantity <= ingredient.getMinimumStockLevel()) {
            return InventoryStatus.CRITICAL;
        } else if (ingredient.getWarningStockLevel() != null && measuredQuantity <= ingredient.getWarningStockLevel()) {
            return InventoryStatus.WARNING;
        } else {
            return InventoryStatus.OK;
        }
    }

    @Override
    public PurchaseOrder generateAutoPurchaseOrder() {
        List<Ingredient> criticalIngredients = ingredientRepository.findAll()
                .stream()
                .filter(i -> checkInventoryStatus(i) == InventoryStatus.CRITICAL)
                .toList();

        PurchaseOrder order = PurchaseOrder.builder()
                .status(OrderStatus.NEW)
                .orderDate(LocalDate.now())
                .note("Automaticky vygenerováno na základě skladových zásob")
                .build();

        List<PurchaseOrderItem> items = criticalIngredients.stream()
                .map(i -> PurchaseOrderItem.builder()
                        .ingredient(i)
                        .quantityOrdered(calculateSuggestedQuantity(i))
                        .status(OrderItemStatus.ORDERED)
                        .purchaseOrder(order)
                        .build())
                .toList();

        order.setItems(items);

        return order;
    }

    private float calculateSuggestedQuantity(Ingredient ingredient) {
        Optional<InventorySnapshot> latestSnapshotOpt =
                inventorySnapshotRepository.findTopByIngredientOrderByTimestampDesc(ingredient);

        float measured = latestSnapshotOpt.map(InventorySnapshot::getMeasuredQuantity).orElse(0f);

        // 1. Pokud máme definovanou "plnou" zásobu – dopočítáme rozdíl
        if (ingredient.getPreferredFullStockLevel() != null) {
            float deficit = ingredient.getPreferredFullStockLevel() - measured;
            return Math.max(deficit, 0f); // nechceme záporné objednávky
        }

        // 2. Jinak – nouzové řešení: objednej 2× minimum
        if (ingredient.getMinimumStockLevel() != null) {
            return ingredient.getMinimumStockLevel() * 2;
        }

        // 3. Poslední možnost: default fallback
        return 1f;
    }

    @Override
    public Optional<IngredientAliasOverviewDto> getAliasOverviewByName(String inputName) {
        return ingredientAliasService.findIngredientByNameFlexible(inputName)
                .map(ingredient -> IngredientAliasOverviewDto.builder()
                        .id(ingredient.getId())
                        .name(ingredient.getName())
                        .aliases(
                                ingredient.getAliases() != null
                                        ? ingredient.getAliases().stream()
                                        .map(IngredientAlias::getAliasName)
                                        .distinct()
                                        .toList()
                                        : List.of()
                        )
                        .build());
    }
}