package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleBulkCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleResponseDto;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.inventory.models.InventorySnapshot;
import leoric.pizzacipollastorage.inventory.repositories.InventorySnapshotRepository;
import leoric.pizzacipollastorage.models.*;
import leoric.pizzacipollastorage.models.enums.DishSize;
import leoric.pizzacipollastorage.models.enums.SnapshotType;
import leoric.pizzacipollastorage.repositories.MenuItemRepository;
import leoric.pizzacipollastorage.repositories.MenuItemSaleLastTimestampRepository;
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
    private final MenuItemSaleLastTimestampRepository menuItemSaleLastTimestampRepository;

    @Override
    @Transactional
    public MenuItemSaleResponseDto createSale(UUID branchId, MenuItemSaleCreateDto dto) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        MenuItem menuItem = menuItemRepository.findByIdAndBranchId(dto.getMenuItemId(), branchId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "MenuItem " + dto.getMenuItemId() + " not found for branch " + branchId));

        DishSize dishSize = dto.getDishSize();

        // 🔑 Přidání aliasu (thirdPartyName), pokud byl poslán a ještě neexistuje
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
                .menuItem(menuItem.getName())
                .dishSize(dishSize)
                .quantitySold(sale.getQuantitySold())
                .cookName(sale.getCookName())
                .saleDate(sale.getSaleDate())
                .build();
    }

    @Override
    @Transactional
    public List<MenuItemSaleResponseDto> createSaleBulk(UUID branchId, MenuItemSaleBulkCreateDto dto) {
        List<MenuItemSaleResponseDto> responses = dto.getItems().stream()
                .map(item -> {
                    MenuItemSaleCreateDto singleDto = new MenuItemSaleCreateDto();
                    singleDto.setMenuItemId(item.getMenuItemId());
                    singleDto.setDishSize(item.getDishSize());
                    singleDto.setQuantitySold(item.getQuantitySold());
                    singleDto.setThirdPartyName(item.getThirdPartyName());
                    singleDto.setCookName(dto.getCookName());
                    singleDto.setLastSaleTimestamp(dto.getLastSaleTimestamp());
                    return createSale(branchId, singleDto);
                })
                .collect(Collectors.toList());

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        MenuItemSaleLastTimestamp entity = menuItemSaleLastTimestampRepository.findByBranchId(branchId)
                .orElseGet(() -> MenuItemSaleLastTimestamp.builder()
                        .branch(branch)
                        .build()
                );

        entity.setLastSaleTimestamp(dto.getLastSaleTimestamp());
        menuItemSaleLastTimestampRepository.save(entity);

        return responses;
    }
}