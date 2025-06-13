package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.StockEntry.StockEntryCreateDto;
import leoric.pizzacipollastorage.DTOs.StockEntry.StockEntryResponseDto;
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

@Service
@RequiredArgsConstructor
public class StockEntryServiceImpl implements StockEntryService {

    private final StockEntryRepository stockEntryRepository;
    private final IngredientRepository ingredientRepository;
    private final SupplierRepository supplierRepository;
    private final StockEntryMapper stockEntryMapper;

    private final InventoryService inventoryService;

    public StockEntryResponseDto createStockEntry(StockEntryCreateDto dto) {
        Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId())
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));

        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

        StockEntry entry = stockEntryMapper.toEntity(dto);
        entry.setIngredient(ingredient);
        entry.setSupplier(supplier);

        StockEntry saved = stockEntryRepository.save(entry);

        inventoryService.addToInventory(ingredient.getId(), dto.getQuantityReceived());

        return stockEntryMapper.toDto(saved);
    }
}