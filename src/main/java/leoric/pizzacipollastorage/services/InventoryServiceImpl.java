package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.InventorySnapshotCreateDto;
import leoric.pizzacipollastorage.DTOs.InventorySnapshotResponseDto;
import leoric.pizzacipollastorage.handler.exceptions.SnapshotTooRecentException;
import leoric.pizzacipollastorage.mapstruct.InventorySnapshotMapper;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.InventorySnapshot;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.repositories.InventorySnapshotRepository;
import leoric.pizzacipollastorage.services.interfaces.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventorySnapshotRepository snapshotRepository;
    private final IngredientRepository ingredientRepository;
    private final InventorySnapshotMapper snapshotMapper;

    @Override
    public InventorySnapshotResponseDto createSnapshot(InventorySnapshotCreateDto dto) {
        Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId())
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));

        LocalDateTime from = dto.getTimestamp().minusHours(6);
        LocalDateTime to = dto.getTimestamp().plusHours(6);

        boolean existsRecent = snapshotRepository.existsByIngredientAndTimestampBetween(ingredient, from, to);
        if (existsRecent) {
            throw new SnapshotTooRecentException("Snapshot for this ingredient already exists in the last 6 hours.");
        }

        InventorySnapshot snapshot = InventorySnapshot.builder()
                .ingredient(ingredient)
                .timestamp(dto.getTimestamp())
                .measuredQuantity(dto.getMeasuredQuantity())
                .note(dto.getNote())
                .build();

        InventorySnapshot saved = snapshotRepository.save(snapshot);
        return snapshotMapper.toDto(saved);
    }
}