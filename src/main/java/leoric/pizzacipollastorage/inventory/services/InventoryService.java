package leoric.pizzacipollastorage.inventory.services;

import leoric.pizzacipollastorage.inventory.dtos.Inventory.InventorySnapshotCreateDto;
import leoric.pizzacipollastorage.inventory.dtos.Inventory.InventorySnapshotResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface InventoryService {

    void addToInventory(UUID branchId, UUID ingredientId, float addedQuantity);

    InventorySnapshotResponseDto createSnapshot(UUID branchId, InventorySnapshotCreateDto dto);

    List<InventorySnapshotResponseDto> createSnapshotBulk(UUID branchId, List<InventorySnapshotCreateDto> dtos);

    List<InventorySnapshotResponseDto> getCurrentInventoryStatus(UUID branchId);

}