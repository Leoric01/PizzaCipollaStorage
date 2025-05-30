package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.InventorySnapshotCreateDto;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.InventorySnapshot;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.repositories.InventorySnapshotRepository;
import leoric.pizzacipollastorage.services.interfaces.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventorySnapshotRepository snapshotRepository;
    private final IngredientRepository ingredientRepository;

    public InventorySnapshot createSnapshot(InventorySnapshotCreateDto dto) {
        Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId())
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));

        InventorySnapshot snapshot = new InventorySnapshot();
        snapshot.setIngredient(ingredient);
        snapshot.setMeasuredQuantity(dto.getMeasuredQuantity());
        snapshot.setTimestamp(dto.getTimestamp());
        snapshot.setNote(dto.getNote());
        return snapshotRepository.save(snapshot);
    }
}