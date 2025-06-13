package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientAlias.IngredientAliasOverviewDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientCreateDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientResponseDto;
import leoric.pizzacipollastorage.handler.exceptions.DuplicateIngredientNameException;
import leoric.pizzacipollastorage.inventory.models.InventorySnapshot;
import leoric.pizzacipollastorage.inventory.repositories.InventorySnapshotRepository;
import leoric.pizzacipollastorage.mapstruct.IngredientMapper;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.IngredientAlias;
import leoric.pizzacipollastorage.models.enums.InventoryStatus;
import leoric.pizzacipollastorage.models.enums.OrderItemStatus;
import leoric.pizzacipollastorage.models.enums.OrderStatus;
import leoric.pizzacipollastorage.purchase.models.PurchaseOrder;
import leoric.pizzacipollastorage.purchase.models.PurchaseOrderItem;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.services.interfaces.IngredientAliasService;
import leoric.pizzacipollastorage.services.interfaces.IngredientService;
import leoric.pizzacipollastorage.vat.models.ProductCategory;
import leoric.pizzacipollastorage.vat.repositories.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final InventorySnapshotRepository inventorySnapshotRepository;

    private final IngredientMapper ingredientMapper;

    private final IngredientAliasService ingredientAliasService;

    @Override
    public IngredientResponseDto updateIngredient(UUID id, IngredientCreateDto dto) {
        Ingredient existing = ingredientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found: " + id));

        if (!existing.getName().equalsIgnoreCase(dto.getName()) &&
            ingredientRepository.existsByName(dto.getName())) {
            throw new DuplicateIngredientNameException("Ingredient with name '" + dto.getName() + "' already exists");
        }

        ProductCategory category = productCategoryRepository
                .findByNameIgnoreCase(dto.getCategory())
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + dto.getCategory()));

        ingredientMapper.update(existing, dto);
        existing.setCategory(category);

        Ingredient saved = ingredientRepository.save(existing);
        return ingredientMapper.toDto(saved);
    }

    @Override
    public IngredientResponseDto createIngredient(IngredientCreateDto dto) {
        if (ingredientRepository.existsByName(dto.getName())) {
            throw new DuplicateIngredientNameException("Ingredient with name '" + dto.getName() + "' already exists");
        }

        ProductCategory category = productCategoryRepository
                .findByNameIgnoreCase(dto.getCategory())
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + dto.getCategory()));

        Ingredient ingredient = ingredientMapper.toEntity(dto);
        ingredient.setCategory(category);

        Ingredient saved = ingredientRepository.save(ingredient);
        return ingredientMapper.toDto(saved);
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
    public List<IngredientResponseDto> getAllIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredientMapper.toDtoList(ingredients);
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