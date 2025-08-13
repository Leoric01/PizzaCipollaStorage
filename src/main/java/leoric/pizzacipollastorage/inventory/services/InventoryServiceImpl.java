package leoric.pizzacipollastorage.inventory.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.inventory.InventorySnapshotMapper;
import leoric.pizzacipollastorage.inventory.dtos.Inventory.InventorySnapshotCreateDto;
import leoric.pizzacipollastorage.inventory.dtos.Inventory.InventorySnapshotResponseDto;
import leoric.pizzacipollastorage.inventory.models.InventorySnapshot;
import leoric.pizzacipollastorage.inventory.repositories.InventorySnapshotRepository;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.enums.IngredientState;
import leoric.pizzacipollastorage.models.enums.SnapshotType;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventorySnapshotRepository snapshotRepository;
    private final IngredientRepository ingredientRepository;
    private final InventorySnapshotMapper inventorySnapshotMapper;
    private final InventoryStatusHelperService inventoryStatusHelperService;
    private final BranchRepository branchRepository;

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

        snapshotRepository.save(snapshot);
    }

    @Override
    public InventorySnapshotResponseDto createSnapshot(UUID branchId, InventorySnapshotCreateDto dto) {
        Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId())
                .filter(i -> i.getBranch().getId().equals(branchId))
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found in this branch"));

        Branch branch = ingredient.getBranch(); // nebo branchRepository.getReferenceById(branchId)

        Float measured = dto.getMeasuredQuantity();
        IngredientState form = dto.getForm() != null ? dto.getForm() : IngredientState.RAW;

        if (form == IngredientState.PREPARED) {
            float lossFactor = ingredient.getLossCleaningFactor();
            if (lossFactor < 0f || lossFactor >= 1f) {
                throw new IllegalArgumentException("Invalid lossCleaningFactor: must be between 0 and 1");
            }
            measured = measured / (1 - lossFactor);
        }

        InventorySnapshot lastSnapshot = snapshotRepository
                .findTopByIngredientAndBranchOrderByTimestampDesc(ingredient, branch)
                .orElse(null);

        Float oldExpected = lastSnapshot != null
                ? (lastSnapshot.getExpectedQuantity() != null
                ? lastSnapshot.getExpectedQuantity()
                : lastSnapshot.getMeasuredQuantity())
                : null;

        Float discrepancy = oldExpected != null ? measured - oldExpected : null;

        LocalDateTime now = LocalDateTime.now();

        InventorySnapshot snapshot = InventorySnapshot.builder()
                .ingredient(ingredient)
                .branch(branch)
                .timestamp(now)
                .measuredQuantity(measured)
                .expectedQuantity(measured)
                .note(dto.getNote() != null
                        ? dto.getNote() + (discrepancy != null ? String.format(" | Discrepancy: %.2f", discrepancy) : "")
                        : (discrepancy != null ? String.format("Discrepancy: %.2f", discrepancy) : "Manual measurement"))
                .type(SnapshotType.INVENTORY)
                .build();

        InventorySnapshot saved = snapshotRepository.save(snapshot);
        return inventorySnapshotMapper.toDto(saved);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<InventorySnapshotResponseDto> getCurrentInventoryStatus(UUID branchId, String search, Pageable pageable) {
        Page<Ingredient> ingredientPage;

        if (search != null && !search.isBlank()) {
            ingredientPage = ingredientRepository.findByBranchIdAndNameContainingIgnoreCase(branchId, search, pageable);
        } else {
            ingredientPage = ingredientRepository.findByBranchId(branchId, pageable);
        }

        return ingredientPage.map(ingredient -> {
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
        });
    }

    @Override
    public List<InventorySnapshotResponseDto> createSnapshotBulk(UUID branchId, List<InventorySnapshotCreateDto> dtos) {
        List<InventorySnapshotResponseDto> results = new ArrayList<>();

        for (InventorySnapshotCreateDto dto : dtos) {
            try {
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