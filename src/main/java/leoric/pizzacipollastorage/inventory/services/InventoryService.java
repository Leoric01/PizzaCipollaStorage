package leoric.pizzacipollastorage.inventory.services;

import leoric.pizzacipollastorage.inventory.dtos.Inventory.InventorySnapshotCreateDto;
import leoric.pizzacipollastorage.inventory.dtos.Inventory.InventorySnapshotResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public interface InventoryService {

    void addToInventory(UUID branchId, UUID ingredientId, float addedQuantity);

    InventorySnapshotResponseDto createSnapshot(UUID branchId, InventorySnapshotCreateDto dto);

    List<InventorySnapshotResponseDto> createSnapshotBulk(UUID branchId, List<InventorySnapshotCreateDto> dtos);

    @Transactional(readOnly = true)
    Page<InventorySnapshotResponseDto> getCurrentInventoryStatusByStream(UUID branchId, String search, Pageable pageable);

//    Page<InventorySnapshotResponseDto> getCurrentInventoryStatus(UUID branchId, String search, Pageable pageable);
//    Page<InventorySnapshotResponseDto> getCurrentInventoryStatus2(UUID branchId, String search, Pageable pageable);

}