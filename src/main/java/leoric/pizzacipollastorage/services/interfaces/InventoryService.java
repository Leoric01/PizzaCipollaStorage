package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.InventorySnapshotCreateDto;
import leoric.pizzacipollastorage.DTOs.InventorySnapshotResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface InventoryService {
    InventorySnapshotResponseDto createSnapshot(InventorySnapshotCreateDto dto);
}