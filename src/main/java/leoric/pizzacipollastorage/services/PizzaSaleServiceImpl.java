package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import leoric.pizzacipollastorage.DTOs.PizzaSale.PizzaSaleCreateDto;
import leoric.pizzacipollastorage.DTOs.PizzaSale.PizzaSaleResponseDto;
import leoric.pizzacipollastorage.models.*;
import leoric.pizzacipollastorage.repositories.*;
import leoric.pizzacipollastorage.services.interfaces.PizzaSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PizzaSaleServiceImpl implements PizzaSaleService {

    private final PizzaSaleRepository pizzaSaleRepository;
    private final PizzaRepository pizzaRepository;
    private final DishSizeRepository dishSizeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final InventorySnapshotRepository inventorySnapshotRepository;

    @Override
    @Transactional
    public PizzaSaleResponseDto createSale(PizzaSaleCreateDto dto) {
        Pizza pizza = pizzaRepository.findById(dto.getPizzaId())
                .orElseThrow(() -> new EntityNotFoundException("Pizza not found"));

        DishSize dishSize = dishSizeRepository.findById(dto.getDishSizeId())
                .orElseThrow(() -> new EntityNotFoundException("Dish size not found"));

        float sizeFactor = dishSize.getFactor();

        // Ulož samotný prodej
        PizzaSale sale = PizzaSale.builder()
                .pizza(pizza)
                .dishSize(dishSize)
                .quantitySold(dto.getQuantitySold())
                .saleDate(LocalDateTime.now())
                .cookName(dto.getCookName())
                .build();
        pizzaSaleRepository.save(sale);

        // Odečti suroviny z inventáře podle receptu
        List<RecipeIngredient> recipe = recipeIngredientRepository.findByPizzaId(pizza.getId());

        for (RecipeIngredient ri : recipe) {
            Ingredient ingredient = ri.getIngredient();

            float usedQuantity = ri.getQuantity() * sizeFactor * dto.getQuantitySold();

            InventorySnapshot lastSnapshot = inventorySnapshotRepository
                    .findTopByIngredientIdOrderByTimestampDesc(ingredient.getId())
                    .orElseThrow(() -> new RuntimeException("Missing inventory snapshot for ingredient: " + ingredient.getName()));

            float newQuantity = lastSnapshot.getMeasuredQuantity() - usedQuantity;

            InventorySnapshot newSnapshot = InventorySnapshot.builder()
                    .ingredient(ingredient)
                    .timestamp(LocalDateTime.now())
                    .measuredQuantity(newQuantity)
                    .note("Pizza sale ID: " + sale.getId())
                    .build();

            inventorySnapshotRepository.save(newSnapshot);
        }

        return PizzaSaleResponseDto.builder()
                .id(sale.getId())
                .pizzaName(pizza.getName())
                .dishSize(dishSize.getName())
                .quantitySold(sale.getQuantitySold())
                .cookName(sale.getCookName())
                .saleDate(sale.getSaleDate())
                .build();
    }
}