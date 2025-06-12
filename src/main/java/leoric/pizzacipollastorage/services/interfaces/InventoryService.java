package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.Inventory.InventorySnapshotCreateDto;
import leoric.pizzacipollastorage.DTOs.Inventory.InventorySnapshotResponseDto;
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