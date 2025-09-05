package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleBulkCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleResponseDto;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.inventory.models.InventorySnapshot;
import leoric.pizzacipollastorage.inventory.repositories.InventorySnapshotRepository;
import leoric.pizzacipollastorage.mapstruct.MenuItemSaleMapper;
import leoric.pizzacipollastorage.models.*;
import leoric.pizzacipollastorage.models.enums.DishSize;
import leoric.pizzacipollastorage.models.enums.IngredientState;
import leoric.pizzacipollastorage.models.enums.SnapshotType;
import leoric.pizzacipollastorage.repositories.*;
import leoric.pizzacipollastorage.services.interfaces.MenuItemSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MenuItemSaleServiceImpl implements MenuItemSaleService {

    private final IngredientRepository ingredientRepository;
    private final MenuitemSaleRepository menuitemSaleRepository;
    private final MenuItemRepository menuItemRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final InventorySnapshotRepository inventorySnapshotRepository;
    private final BranchRepository branchRepository;
    private final MenuItemSaleLastTimestampRepository menuItemSaleLastTimestampRepository;
    private final IgnoredThirdPartyNameRepository ignoredThirdPartyNameRepository;
    private final MenuItemSaleMapper menuItemSaleMapper;

    @Override
    @Transactional
    public MenuItemSaleResponseDto createSale(UUID branchId, MenuItemSaleCreateDto dto) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        MenuItem menuItem = menuItemRepository.findByIdAndBranchId(dto.getMenuItemId(), branchId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "MenuItem " + dto.getMenuItemId() + " not found for branch " + branchId));

        DishSize dishSize = dto.getDishSize();

        if (dto.getThirdPartyName() != null
            && !dto.getThirdPartyName().isBlank()
            && !menuItem.getThirdPartyNames().contains(dto.getThirdPartyName())) {
            menuItem.getThirdPartyNames().add(dto.getThirdPartyName());
            menuItemRepository.save(menuItem);
        }

        MenuItemSale sale = MenuItemSale.builder()
                .menuItem(menuItem)
                .dishSize(dishSize)
                .quantitySold(dto.getQuantitySold())
                .saleDate(LocalDateTime.now())
                .cookName(dto.getCookName())
                .branch(branch)
                .build();
        menuitemSaleRepository.save(sale);

        List<RecipeIngredient> recipe = recipeIngredientRepository.findByMenuItemId(menuItem.getId());
        for (RecipeIngredient ri : recipe) {
            Ingredient ingredient = ri.getIngredient();
            float usedQuantity = ri.getQuantity() * dto.getQuantitySold();

            InventorySnapshot lastSnapshot = inventorySnapshotRepository
                    .findTopByIngredientIdAndBranchIdOrderByTimestampDesc(ingredient.getId(), branchId)
                    .orElseThrow(() -> new RuntimeException(
                            "Missing inventory snapshot for ingredient: " + ingredient.getName() + " in branch " + branchId));

            Float lastExpected = lastSnapshot.getExpectedQuantity() != null
                    ? lastSnapshot.getExpectedQuantity()
                    : lastSnapshot.getMeasuredQuantity();

            float newExpected = lastExpected - usedQuantity;

            InventorySnapshot expectedUpdate = InventorySnapshot.builder()
                    .ingredient(ingredient)
                    .branch(branch)
                    .timestamp(LocalDateTime.now())
                    .expectedQuantity(newExpected)
                    .measuredQuantity(lastSnapshot.getMeasuredQuantity())
                    .note("Expected deduction - sale ID: " + sale.getId())
                    .type(SnapshotType.SYSTEM)
                    .build();

            inventorySnapshotRepository.save(expectedUpdate);
        }

        return MenuItemSaleResponseDto.builder()
                .id(sale.getId())
                .menuItemName(menuItem.getName())
                .dishSize(dishSize)
                .quantitySold(sale.getQuantitySold())
                .cookName(sale.getCookName())
                .saleDate(sale.getSaleDate())
                .build();
    }

    @Override
    @Transactional
    public List<MenuItemSaleResponseDto> createSaleBulk(UUID branchId, MenuItemSaleBulkCreateDto dto) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        Map<UUID, Float> ingredientUsageMap = new HashMap<>();
        List<MenuItemSale> salesList = new ArrayList<>();

        for (MenuItemSaleBulkCreateDto.MenuItemSaleBulkItemDto item : dto.getItems()) {
            MenuItem menuItem = getMenuItemOrThrow(branchId, item.getMenuItemId());
            handleThirdPartyAlias(branchId, menuItem, item.getThirdPartyName());

            MenuItemSale sale = createSaleEntity(branch, menuItem, item, dto.getCookName());
            salesList.add(sale);

            accumulateIngredientUsage(ingredientUsageMap, menuItem, item.getQuantitySold());
        }

        menuitemSaleRepository.saveAll(salesList);
        saveInventorySnapshots(branch, ingredientUsageMap, dto.getItems().size());
        updateLastSaleTimestamp(branch, dto.getLastSaleTimestamp());

        return menuItemSaleMapper.toDtoList(salesList);
    }

    private MenuItem getMenuItemOrThrow(UUID branchId, UUID menuItemId) {
        return menuItemRepository.findByIdAndBranchId(menuItemId, branchId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "MenuItem " + menuItemId + " not found for branch " + branchId));
    }

    private void handleThirdPartyAlias(UUID branchId, MenuItem menuItem, String thirdPartyName) {
        if (thirdPartyName == null || thirdPartyName.isBlank()) return;

        if (ignoredThirdPartyNameRepository.existsByBranchIdAndName(branchId, thirdPartyName)) {
            ignoredThirdPartyNameRepository.deleteByBranchIdAndName(branchId, thirdPartyName);
        }

        if (!menuItem.getThirdPartyNames().contains(thirdPartyName)) {
            List<MenuItem> itemsWithSameAlias = menuItemRepository.findAllByBranchIdAndThirdPartyName(branchId, thirdPartyName);
            for (MenuItem otherItem : itemsWithSameAlias) {
                if (!otherItem.getId().equals(menuItem.getId())) {
                    otherItem.getThirdPartyNames().remove(thirdPartyName);
                    menuItemRepository.save(otherItem);
                }
            }
            menuItem.getThirdPartyNames().add(thirdPartyName);
            menuItemRepository.save(menuItem);
        }
    }

    private MenuItemSale createSaleEntity(Branch branch,
                                          MenuItem menuItem,
                                          MenuItemSaleBulkCreateDto.MenuItemSaleBulkItemDto item,
                                          String cookName) {
        return MenuItemSale.builder()
                .menuItem(menuItem)
                .dishSize(item.getDishSize())
                .quantitySold(item.getQuantitySold())
                .saleDate(LocalDateTime.now())
                .cookName(cookName)
                .branch(branch)
                .build();
    }

    private void accumulateIngredientUsage(Map<UUID, Float> usageMap, MenuItem menuItem, float quantitySold) {
        List<RecipeIngredient> recipe = recipeIngredientRepository.findByMenuItemId(menuItem.getId());
        for (RecipeIngredient ri : recipe) {
            usageMap.merge(ri.getIngredient().getId(), ri.getQuantity() * quantitySold, Float::sum);
        }
    }

    private void saveInventorySnapshots(Branch branch, Map<UUID, Float> usageMap, int saleCount) {
        List<InventorySnapshot> snapshotsToSave = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Map.Entry<UUID, Float> entry : usageMap.entrySet()) {
            UUID ingredientId = entry.getKey();
            float totalUsed = entry.getValue();

            Ingredient ingredient = ingredientRepository.findById(ingredientId)
                    .orElseThrow(() -> new RuntimeException("Ingredient not found: " + ingredientId));

            InventorySnapshot lastSnapshot = inventorySnapshotRepository
                    .findTopByIngredientIdAndBranchIdOrderByTimestampDesc(ingredientId, branch.getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Missing inventory snapshot for ingredient: " + ingredient.getName() + " in branch " + branch.getId()));

            Float lastExpected = lastSnapshot.getExpectedQuantity() != null
                    ? lastSnapshot.getExpectedQuantity()
                    : lastSnapshot.getMeasuredQuantity();

            snapshotsToSave.add(InventorySnapshot.builder()
                    .ingredient(ingredient)
                    .form(IngredientState.RAW)
                    .branch(branch)
                    .timestamp(now)
                    .expectedQuantity(lastExpected - totalUsed)
                    .measuredQuantity(lastSnapshot.getMeasuredQuantity())
                    .note("Bulk sale deduction - " + saleCount + " sales")
                    .type(SnapshotType.SYSTEM)
                    .build());
        }

        inventorySnapshotRepository.saveAll(snapshotsToSave);
    }

    private void updateLastSaleTimestamp(Branch branch, LocalDateTime lastSaleTimestamp) {
        MenuItemSaleLastTimestamp entity = menuItemSaleLastTimestampRepository.findByBranchId(branch.getId())
                .orElseGet(() -> MenuItemSaleLastTimestamp.builder().branch(branch).build());

        entity.setLastSaleTimestamp(lastSaleTimestamp);
        menuItemSaleLastTimestampRepository.save(entity);
    }
}