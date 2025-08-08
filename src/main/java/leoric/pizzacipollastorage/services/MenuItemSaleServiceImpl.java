package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleResponseDto;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.inventory.models.InventorySnapshot;
import leoric.pizzacipollastorage.inventory.repositories.InventorySnapshotRepository;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.MenuItem;
import leoric.pizzacipollastorage.models.MenuItemSale;
import leoric.pizzacipollastorage.models.RecipeIngredient;
import leoric.pizzacipollastorage.models.enums.DishSize;
import leoric.pizzacipollastorage.models.enums.SnapshotType;
import leoric.pizzacipollastorage.repositories.MenuItemRepository;
import leoric.pizzacipollastorage.repositories.MenuitemSaleRepository;
import leoric.pizzacipollastorage.repositories.RecipeIngredientRepository;
import leoric.pizzacipollastorage.services.interfaces.MenuItemSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemSaleServiceImpl implements MenuItemSaleService {

    private final MenuitemSaleRepository menuitemSaleRepository;
    private final MenuItemRepository menuItemRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final InventorySnapshotRepository inventorySnapshotRepository;
    private final BranchRepository branchRepository;

    @Override
    @Transactional
    public MenuItemSaleResponseDto createSale(UUID branchId, MenuItemSaleCreateDto dto) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));
        // Najdi menu položku v rámci pobočky
        MenuItem menuItem = menuItemRepository.findByIdAndBranchId(dto.getMenuItemId(), branchId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "MenuItem " + dto.getMenuItemId() + " not found for branch " + branchId));

        DishSize dishSize = dto.getDishSize();

        // Ulož samotný prodej
        MenuItemSale sale = MenuItemSale.builder()
                .menuItem(menuItem)
                .dishSize(dishSize)
                .quantitySold(dto.getQuantitySold())
                .saleDate(LocalDateTime.now())
                .cookName(dto.getCookName())
                .branch(branch)
                .build();
        menuitemSaleRepository.save(sale);

        // Odečti suroviny z inventáře podle receptu
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
                    : lastSnapshot.getMeasuredQuantity(); // první snapshot ještě nemusí mít expected

            float newExpected = lastExpected - usedQuantity;

            InventorySnapshot expectedUpdate = InventorySnapshot.builder()
                    .ingredient(ingredient)
                    .branch(branch)
                    .timestamp(LocalDateTime.now())
                    .expectedQuantity(newExpected)
                    .measuredQuantity(lastSnapshot.getMeasuredQuantity()) // neměníme
                    .note("Expected deduction - sale ID: " + sale.getId())
                    .type(SnapshotType.SYSTEM)
                    .build();

            inventorySnapshotRepository.save(expectedUpdate);
        }

        return MenuItemSaleResponseDto.builder()
                .id(sale.getId())
                .menuItem(menuItem.getName())
                .dishSize(dishSize)
                .quantitySold(sale.getQuantitySold())
                .cookName(sale.getCookName())
                .saleDate(sale.getSaleDate())
                .build();
    }

    @Override
    @Transactional
    public List<MenuItemSaleResponseDto> createSaleBulk(UUID branchId, List<MenuItemSaleCreateDto> dtos) {

        return dtos.stream()
                .map(dto -> createSale(branchId, dto))
                .collect(Collectors.toList());
    }
}