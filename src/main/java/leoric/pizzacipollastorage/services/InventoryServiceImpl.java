package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.Inventory.InventorySnapshotCreateDto;
import leoric.pizzacipollastorage.DTOs.Inventory.InventorySnapshotResponseDto;
import leoric.pizzacipollastorage.handler.exceptions.SnapshotTooRecentException;
import leoric.pizzacipollastorage.mapstruct.InventorySnapshotMapper;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.InventorySnapshot;
import leoric.pizzacipollastorage.models.enums.SnapshotType;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.repositories.InventorySnapshotRepository;
import leoric.pizzacipollastorage.services.interfaces.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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

        InventorySnapshot lastSnapshot = snapshotRepository
                .findTopByIngredientOrderByTimestampDesc(ingredient)
                .orElse(null);

        Float discrepancy = null;
        if (dto.getMeasuredQuantity() != null) {
            Float oldExpected = lastSnapshot != null
                    ? (lastSnapshot.getExpectedQuantity() != null
                    ? lastSnapshot.getExpectedQuantity()
                    : lastSnapshot.getMeasuredQuantity())
                    : null;

            discrepancy = oldExpected != null
                    ? dto.getMeasuredQuantity() - oldExpected
                    : null;
        }

        InventorySnapshot snapshot = InventorySnapshot.builder()
                .ingredient(ingredient)
                .timestamp(dto.getTimestamp())
                .measuredQuantity(dto.getMeasuredQuantity())
                .expectedQuantity(dto.getMeasuredQuantity()) // reset očekávaného množství na realitu
                .note(dto.getNote() != null
                        ? dto.getNote() + (discrepancy != null ? String.format(" | Discrepancy: %.2f", discrepancy) : "")
                        : (discrepancy != null ? String.format("Discrepancy: %.2f", discrepancy) : "Manual measurement"))
                .type(SnapshotType.INVENTORY)
                .build();

        InventorySnapshot saved = snapshotRepository.save(snapshot);
        return snapshotMapper.toDto(saved);
    }

    @Override
    public List<InventorySnapshotResponseDto> getCurrentInventoryStatus() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        List<InventorySnapshotResponseDto> result = new ArrayList<>();

        for (Ingredient ingredient : ingredients) {
            Optional<InventorySnapshot> snapshotOpt = snapshotRepository.findTopByIngredientOrderByTimestampDesc(ingredient);

            if (snapshotOpt.isPresent()) {
                InventorySnapshot snapshot = snapshotOpt.get();
                result.add(snapshotMapper.toDto(snapshot));
            } else {
                // pokud nemám snapshot, můžu vytvořit „prázdný“ DTO s nulovým množstvím
                InventorySnapshot emptySnapshot = InventorySnapshot.builder()
                        .ingredient(ingredient)
                        .timestamp(LocalDateTime.now())
                        .measuredQuantity(0f)
                        .expectedQuantity(null)
                        .note("No snapshot available")
                        .build();

                result.add(snapshotMapper.toDto(emptySnapshot));
            }
        }

        return result;
    }

    @Override
    public void addToInventory(Long ingredientId, float addedQuantity) {
        InventorySnapshotResponseDto current = getCurrentInventoryStatusMap().get(ingredientId);

        float prevExpected = current.getExpectedQuantity() != null
                ? current.getExpectedQuantity()
                : current.getMeasuredQuantity();

        float newExpected = prevExpected + addedQuantity;
        float newMeasured = current.getMeasuredQuantity() + addedQuantity;

        InventorySnapshot snapshot = InventorySnapshot.builder()
                .ingredient(ingredientRepository.getReferenceById(ingredientId))
                .timestamp(LocalDateTime.now())
                .expectedQuantity(newExpected)
                .measuredQuantity(newMeasured)
                .note("Stock received")
                .type(SnapshotType.STOCK_IN)
                .build();

        snapshotRepository.save(snapshot);
    }

    public Map<Long, InventorySnapshotResponseDto> getCurrentInventoryStatusMap() {
        return getCurrentInventoryStatus().stream()
                .collect(Collectors.toMap(
                        dto -> dto.getIngredient().getId(),
                        Function.identity()
                ));
    }
}