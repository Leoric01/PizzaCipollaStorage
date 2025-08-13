package leoric.pizzacipollastorage.inventory.services;

import leoric.pizzacipollastorage.inventory.InventorySnapshotMapper;
import leoric.pizzacipollastorage.inventory.dtos.Inventory.InventorySnapshotResponseDto;
import leoric.pizzacipollastorage.inventory.models.InventorySnapshot;
import leoric.pizzacipollastorage.inventory.repositories.InventorySnapshotRepository;
import leoric.pizzacipollastorage.models.enums.SnapshotType;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryStatusHelperService {
    private final IngredientRepository ingredientRepository;
    private final InventorySnapshotRepository snapshotRepository;
    private final InventorySnapshotMapper inventorySnapshotMapper;

    @Transactional(readOnly = true)
    public Map<UUID, InventorySnapshotResponseDto> getCurrentInventoryStatusMap(UUID branchId) {
        return ingredientRepository.findByBranchId(branchId, Pageable.unpaged())
                .map(ingredient -> {
                    Optional<InventorySnapshot> snapshotOpt =
                            snapshotRepository.findTopByIngredientAndBranchOrderByTimestampDesc(ingredient, ingredient.getBranch());

                    InventorySnapshot snapshot = snapshotOpt.orElseGet(() ->
                            InventorySnapshot.builder()
                                    .ingredient(ingredient)
                                    .branch(ingredient.getBranch())
                                    .timestamp(LocalDateTime.now())
                                    .measuredQuantity(0f)
                                    .expectedQuantity(null)
                                    .note("No snapshot available")
                                    .type(SnapshotType.INVENTORY)
                                    .build()
                    );

                    return inventorySnapshotMapper.toDto(snapshot);
                })
                .stream()
                .collect(Collectors.toMap(
                        dto -> dto.getIngredient().getId(),
                        Function.identity()
                ));
    }
}