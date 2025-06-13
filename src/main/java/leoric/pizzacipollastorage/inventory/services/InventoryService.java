package leoric.pizzacipollastorage.inventory.services;

import leoric.pizzacipollastorage.inventory.dtos.Inventory.InventorySnapshotCreateDto;
import leoric.pizzacipollastorage.inventory.dtos.Inventory.InventorySnapshotResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public interface InventoryService {
    InventorySnapshotResponseDto createSnapshot(InventorySnapshotCreateDto dto);

    List<InventorySnapshotResponseDto> getCurrentInventoryStatus();

    void addToInventory(UUID ingredientId, float addedQuantity);

    Map<UUID, InventorySnapshotResponseDto> getCurrentInventoryStatusMap();

    List<InventorySnapshotResponseDto> createSnapshotBulk(List<InventorySnapshotCreateDto> dtos);
}