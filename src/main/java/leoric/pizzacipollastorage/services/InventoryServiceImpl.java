package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientInventoryDto;
import leoric.pizzacipollastorage.DTOs.Inventory.InventorySnapshotCreateDto;
import leoric.pizzacipollastorage.DTOs.Inventory.InventorySnapshotResponseDto;
import leoric.pizzacipollastorage.handler.exceptions.SnapshotTooRecentException;
import leoric.pizzacipollastorage.mapstruct.IngredientMapper;
import leoric.pizzacipollastorage.mapstruct.InventorySnapshotMapper;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.InventorySnapshot;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.repositories.InventorySnapshotRepository;
import leoric.pizzacipollastorage.services.interfaces.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventorySnapshotRepository snapshotRepository;
    private final IngredientRepository ingredientRepository;
    private final InventorySnapshotMapper snapshotMapper;
    private final IngredientMapper ingredientMapper;

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

    @Override
    public List<IngredientInventoryDto> getCurrentInventoryStatus() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        List<IngredientInventoryDto> inventory = new ArrayList<>();

        for (Ingredient ingredient : ingredients) {
            Optional<InventorySnapshot> latestSnapshotOpt = snapshotRepository.findTopByIngredientOrderByTimestampDesc(ingredient);

            IngredientInventoryDto dto = ingredientMapper.toInventoryDto(ingredient);

            if (latestSnapshotOpt.isPresent()) {
                dto.setMeasuredQuantity(latestSnapshotOpt.get().getMeasuredQuantity());
            } else {
                dto.setMeasuredQuantity(0);
            }

            inventory.add(dto);
        }

        return inventory;
    }
}