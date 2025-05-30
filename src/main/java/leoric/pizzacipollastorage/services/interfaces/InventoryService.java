package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.InventorySnapshotCreateDto;
import leoric.pizzacipollastorage.models.InventorySnapshot;
import org.springframework.stereotype.Service;

@Service
public interface InventoryService {
    InventorySnapshot createSnapshot(InventorySnapshotCreateDto dto);
}