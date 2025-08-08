package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.StockEntry.StockEntryCreateDto;
import leoric.pizzacipollastorage.DTOs.StockEntry.StockEntryResponseDto;
import leoric.pizzacipollastorage.handler.exceptions.NotAuthorizedForBranchException;
import leoric.pizzacipollastorage.inventory.services.InventoryService;
import leoric.pizzacipollastorage.mapstruct.StockEntryMapper;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.StockEntry;
import leoric.pizzacipollastorage.purchase.models.Supplier;
import leoric.pizzacipollastorage.purchase.repositories.SupplierRepository;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.repositories.StockEntryRepository;
import leoric.pizzacipollastorage.services.interfaces.StockEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockEntryServiceImpl implements StockEntryService {

    private final StockEntryRepository stockEntryRepository;
    private final IngredientRepository ingredientRepository;
    private final SupplierRepository supplierRepository;
    private final StockEntryMapper stockEntryMapper;
    private final InventoryService inventoryService;

    @Override
    public StockEntryResponseDto stockEntryCreate(UUID branchId, StockEntryCreateDto dto) {
        Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId())
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));

        if (!ingredient.getBranch().getId().equals(branchId)) {
            throw new NotAuthorizedForBranchException("Ingredient does not belong to this branch.");
        }

        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

        StockEntry entry = stockEntryMapper.toEntity(dto);
        entry.setIngredient(ingredient);
        entry.setSupplier(supplier);

        StockEntry saved = stockEntryRepository.save(entry);

        inventoryService.addToInventory(branchId, ingredient.getId(), dto.getQuantityReceived());

        return stockEntryMapper.toDto(saved);
    }

    @Override
    @Transactional
    public List<StockEntryResponseDto> createStockEntries(UUID branchId, List<StockEntryCreateDto> dtos) {
        // Načti všechny potřebné ID
        List<UUID> ingredientIds = dtos.stream()
                .map(StockEntryCreateDto::getIngredientId)
                .toList();
        List<UUID> supplierIds = dtos.stream()
                .map(StockEntryCreateDto::getSupplierId)
                .toList();

        // Načti hromadně ingredience a dodavatele
        Map<UUID, Ingredient> ingredientsMap = ingredientRepository.findAllById(ingredientIds).stream()
                .collect(Collectors.toMap(Ingredient::getId, i -> i));

        Map<UUID, Supplier> suppliersMap = supplierRepository.findAllById(supplierIds).stream()
                .collect(Collectors.toMap(Supplier::getId, s -> s));

        // Validace, mapování DTO -> entity
        List<StockEntry> stockEntries = new ArrayList<>();
        for (StockEntryCreateDto dto : dtos) {
            Ingredient ingredient = ingredientsMap.get(dto.getIngredientId());
            if (ingredient == null) {
                throw new EntityNotFoundException("Ingredient not found: " + dto.getIngredientId());
            }
            if (!ingredient.getBranch().getId().equals(branchId)) {
                throw new NotAuthorizedForBranchException(
                        "Ingredient does not belong to this branch: " + dto.getIngredientId());
            }

            Supplier supplier = suppliersMap.get(dto.getSupplierId());
            if (supplier == null) {
                throw new EntityNotFoundException("Supplier not found: " + dto.getSupplierId());
            }

            StockEntry entry = stockEntryMapper.toEntity(dto);
            entry.setIngredient(ingredient);
            entry.setSupplier(supplier);
            stockEntries.add(entry);
        }

        // Ulož vše najednou
        List<StockEntry> savedEntries = stockEntryRepository.saveAll(stockEntries);

        // Aktualizuj inventory pro každou položku
        for (int i = 0; i < savedEntries.size(); i++) {
            StockEntryCreateDto dto = dtos.get(i);
            inventoryService.addToInventory(branchId, dto.getIngredientId(), dto.getQuantityReceived());
        }

        // Vrátit DTO výsledek
        return savedEntries.stream()
                .map(stockEntryMapper::toDto)
                .toList();
    }
}