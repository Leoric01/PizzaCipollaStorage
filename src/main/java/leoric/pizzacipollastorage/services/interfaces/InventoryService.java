package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.Inventory.InventorySnapshotCreateDto;
import leoric.pizzacipollastorage.DTOs.Inventory.InventorySnapshotResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface InventoryService {
    InventorySnapshotResponseDto createSnapshot(InventorySnapshotCreateDto dto);

    List<InventorySnapshotResponseDto> getCurrentInventoryStatus();

    void addToInventory(Long ingredientId, float addedQuantity);

    Map<Long, InventorySnapshotResponseDto> getCurrentInventoryStatusMap();
}