package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientInventoryDto;
import leoric.pizzacipollastorage.DTOs.Inventory.InventorySnapshotCreateDto;
import leoric.pizzacipollastorage.DTOs.Inventory.InventorySnapshotResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InventoryService {
    InventorySnapshotResponseDto createSnapshot(InventorySnapshotCreateDto dto);
    List<IngredientInventoryDto> getCurrentInventoryStatus();

}