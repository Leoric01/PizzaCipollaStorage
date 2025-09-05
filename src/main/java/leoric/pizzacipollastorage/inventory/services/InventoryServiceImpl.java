package leoric.pizzacipollastorage.inventory.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.inventory.InventorySnapshotMapper;
import leoric.pizzacipollastorage.inventory.dtos.Inventory.InventorySnapshotCreateDto;
import leoric.pizzacipollastorage.inventory.dtos.Inventory.InventorySnapshotResponseDto;
import leoric.pizzacipollastorage.inventory.models.InventorySnapshot;
import leoric.pizzacipollastorage.inventory.repositories.InventorySnapshotRepository;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.enums.IngredientState;
import leoric.pizzacipollastorage.models.enums.SnapshotType;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.utils.CustomUtilityString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventorySnapshotRepository inventorySnapshotRepository;
    private final IngredientRepository ingredientRepository;
    private final InventorySnapshotMapper inventorySnapshotMapper;
    private final InventoryStatusHelperService inventoryStatusHelperService;

    @Override
    public void addToInventory(UUID branchId, UUID ingredientId, float addedQuantity) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .filter(i -> i.getBranch().getId().equals(branchId))
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found in this branch"));

        InventorySnapshotResponseDto current = inventoryStatusHelperService.getCurrentInventoryStatusMap(branchId).get(ingredientId);

        float prevExpected = current.getExpectedQuantity() != null
                ? current.getExpectedQuantity()
                : current.getMeasuredQuantity();

        float newExpected = prevExpected + addedQuantity;
        float newMeasured = current.getMeasuredQuantity() + addedQuantity;

        InventorySnapshot snapshot = InventorySnapshot.builder()
                .ingredient(ingredient)
                .branch(ingredient.getBranch())
                .timestamp(LocalDateTime.now())
                .expectedQuantity(newExpected)
                .measuredQuantity(newMeasured)
                .note("Stock received")
                .type(SnapshotType.STOCK_IN)
                .build();

        inventorySnapshotRepository.save(snapshot);
    }

    @Override
    public InventorySnapshotResponseDto createSnapshot(UUID branchId, InventorySnapshotCreateDto dto) {
        Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId())
                .filter(i -> i.getBranch().getId().equals(branchId))
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found in this branch"));

        Branch branch = ingredient.getBranch();

        Float measured = dto.getMeasuredQuantity();
        IngredientState form = dto.getForm() != null ? dto.getForm() : IngredientState.RAW;

        if (form == IngredientState.PREPARED) {
            float lossFactor = ingredient.getLossCleaningFactor();
            if (lossFactor < 0f || lossFactor >= 1f) {
                throw new IllegalArgumentException("Invalid lossCleaningFactor: must be between 0 and 1");
            }
            measured = measured / (1 - lossFactor);
        }

        InventorySnapshot lastSnapshot = inventorySnapshotRepository
                .findTopByIngredientAndBranchOrderByTimestampDesc(ingredient, branch)
                .orElse(null);

        Float oldExpected = lastSnapshot != null
                ? (lastSnapshot.getExpectedQuantity() != null
                ? lastSnapshot.getExpectedQuantity()
                : lastSnapshot.getMeasuredQuantity())
                : null;

        Float discrepancy = oldExpected != null ? measured - oldExpected : null;

        LocalDateTime now = LocalDateTime.now();
        String baseNote = "Poslední měření proběhlo " + now;
        String discrepancyNote = discrepancy != null ? String.format(" | Odchylka: %.2f", discrepancy) : "";

        InventorySnapshot snapshot = InventorySnapshot.builder()
                .ingredient(ingredient)
                .branch(branch)
                .timestamp(now)
                .measuredQuantity(measured)
                .expectedQuantity(measured)
                .lastDiscrepancy(discrepancy)
                .note(baseNote + discrepancyNote)
                .type(SnapshotType.INVENTORY)
                .build();

        InventorySnapshot saved = inventorySnapshotRepository.save(snapshot);

        return inventorySnapshotMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InventorySnapshotResponseDto> getCurrentInventoryStatusByStream(UUID branchId, String search, Pageable pageable) {
        List<InventorySnapshot> allSnapshots = inventorySnapshotRepository.findByBranchId(branchId);

        Map<UUID, Optional<InventorySnapshot>> latestSnapshotsByIngredient = allSnapshots.stream()
                .filter(snapshot -> search == null ||
                                    CustomUtilityString.normalize(snapshot.getIngredient().getName())
                                            .contains(CustomUtilityString.normalize(search)))
                .collect(Collectors.groupingBy(
                        snapshot -> snapshot.getIngredient().getId(),
                        Collectors.maxBy(Comparator.comparing(InventorySnapshot::getTimestamp))
                ));

        List<InventorySnapshot> latestSnapshots = latestSnapshotsByIngredient.values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        Comparator<InventorySnapshot> comparator = pageable.getSort().stream()
                .map(order -> {
                    Comparator<InventorySnapshot> cmp;
                    switch (order.getProperty()) {
                        // SAME AS DEFAULT
//                        case "ingredientName" ->
//                                cmp = Comparator.comparing(
//                                        s -> CustomUtilityString.normalize(s.getIngredient().getName()),
//                                        Comparator.nullsLast(String::compareTo)
//                                );
                        case "timestamp" -> cmp = Comparator.comparing(
                                InventorySnapshot::getTimestamp,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        );
                        case "measuredQuantity" -> cmp = Comparator.comparing(
                                InventorySnapshot::getMeasuredQuantity,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        );
                        case "expectedQuantity" -> cmp = Comparator.comparing(
                                InventorySnapshot::getExpectedQuantity,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        );
                        case "lastDiscrepancy" -> cmp = Comparator.comparing(
                                InventorySnapshot::getLastDiscrepancy,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        );
                        case "type" -> cmp = Comparator.comparing(
                                s -> CustomUtilityString.normalize(s.getType().name()),
                                Comparator.nullsLast(String::compareTo)
                        );
                        case "note" -> cmp = Comparator.comparing(
                                s -> CustomUtilityString.normalize(s.getNote()),
                                Comparator.nullsLast(String::compareTo)
                        );
                        default -> cmp = Comparator.comparing(
                                s -> CustomUtilityString.normalize(s.getIngredient().getName()),
                                Comparator.nullsLast(String::compareTo)
                        );
                    }
                    return order.isAscending() ? cmp : cmp.reversed();
                })
                .reduce(Comparator::thenComparing)
                .orElse(Comparator.comparing(
                        s -> CustomUtilityString.normalize(s.getIngredient().getName()),
                        Comparator.nullsLast(String::compareTo)
                ));

        latestSnapshots.sort(comparator);

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), latestSnapshots.size());
        List<InventorySnapshotResponseDto> pagedDtoList = latestSnapshots.subList(start, end).stream()
                .map(inventorySnapshotMapper::toDto)
                .toList();

        return new PageImpl<>(pagedDtoList, pageable, latestSnapshots.size());
    }

    @Override
    public List<InventorySnapshotResponseDto> createSnapshotBulk(UUID branchId, List<InventorySnapshotCreateDto> dtos) {
        List<InventorySnapshotResponseDto> results = new ArrayList<>();

        for (InventorySnapshotCreateDto dto : dtos) {
            try {
                if (dto.getIngredientId() == null) {
                    continue;
                }
                results.add(createSnapshot(branchId, dto));
            } catch (EntityNotFoundException e) {
                log.error("Ingredient not found for ID: {}", dto.getIngredientId(), e);
            } catch (Exception e) {
                log.error("Unexpected error while creating snapshot for ingredientId={} : {}",
                        dto.getIngredientId(), e.getMessage(), e);
            }
        }

        return results;
    }

}