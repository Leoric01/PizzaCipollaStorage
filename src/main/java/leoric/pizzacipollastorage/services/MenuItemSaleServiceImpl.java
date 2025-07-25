package leoric.pizzacipollastorage.services;

import jakarta.transaction.Transactional;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleResponseDto;
import leoric.pizzacipollastorage.inventory.repositories.InventorySnapshotRepository;
import leoric.pizzacipollastorage.repositories.MenuItemRepository;
import leoric.pizzacipollastorage.repositories.MenuitemSaleRepository;
import leoric.pizzacipollastorage.repositories.RecipeIngredientRepository;
import leoric.pizzacipollastorage.services.interfaces.MenuItemSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemSaleServiceImpl implements MenuItemSaleService {

    private final MenuitemSaleRepository menuitemSaleRepository;
    private final MenuItemRepository menuItemRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final InventorySnapshotRepository inventorySnapshotRepository;

    @Override
    @Transactional
    public MenuItemSaleResponseDto createSale(MenuItemSaleCreateDto dto) {
//        MenuItem menuItem = menuItemRepository.findById(dto.getMenuItemId())
//                .orElseThrow(() -> new EntityNotFoundException("MenuItem " + dto.getMenuItemId() + " not found"));
//
//        DishSize dishSize = dishSizeRepository.findById(dto.getDishSizeId())
//                .orElseThrow(() -> new EntityNotFoundException("Dish size " + dto.getDishSizeId() + " not found"));
//
//        float sizeFactor = dishSize.getFactor();
//
//        // Ulož samotný prodej
//        MenuItemSale sale = MenuItemSale.builder()
//                .menuItem(menuItem)
//                .dishSize(dishSize)
//                .quantitySold(dto.getQuantitySold())
//                .saleDate(LocalDateTime.now())
//                .cookName(dto.getCookName())
//                .build();
//        menuitemSaleRepository.save(sale);
//
//        // Odečti suroviny z inventáře podle receptu
//        List<RecipeIngredient> recipe = recipeIngredientRepository.findByMenuItemId(menuItem.getId());
//
//        for (RecipeIngredient ri : recipe) {
//            Ingredient ingredient = ri.getIngredient();
//            float usedQuantity = ri.getQuantity() * sizeFactor * dto.getQuantitySold();
//
//            InventorySnapshot lastSnapshot = inventorySnapshotRepository
//                    .findTopByIngredientIdOrderByTimestampDesc(ingredient.getId())
//                    .orElseThrow(() -> new RuntimeException("Missing inventory snapshot for ingredient: " + ingredient.getName()));
//
//            Float lastExpected = lastSnapshot.getExpectedQuantity() != null
//                    ? lastSnapshot.getExpectedQuantity()
//                    : lastSnapshot.getMeasuredQuantity(); // první snapshot ještě nemusí mít expected
//
//            float newExpected = lastExpected - usedQuantity;
//
//            InventorySnapshot expectedUpdate = InventorySnapshot.builder()
//                    .ingredient(ingredient)
//                    .timestamp(LocalDateTime.now())
//                    .expectedQuantity(newExpected)
//                    .measuredQuantity(lastSnapshot.getMeasuredQuantity()) // neměníme
//                    .note("Expected deduction - sale ID: " + sale.getId())
//                    .type(SnapshotType.SYSTEM)
//                    .build();
//
//            inventorySnapshotRepository.save(expectedUpdate);
//        }
//
//        return MenuItemSaleResponseDto.builder()
//                .id(sale.getId())
//                .menuItem(menuItem.getName())
//                .dishSize(dishSize.getName())
//                .quantitySold(sale.getQuantitySold())
//                .cookName(sale.getCookName())
//                .saleDate(sale.getSaleDate())
//                .build();
        return null;
    }

    @Override
    @Transactional
    public List<MenuItemSaleResponseDto> createSaleBulk(List<MenuItemSaleCreateDto> dtos) {
        return dtos.stream()
                .map(this::createSale)
                .collect(Collectors.toList());
    }
}