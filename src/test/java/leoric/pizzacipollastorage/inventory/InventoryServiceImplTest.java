package leoric.pizzacipollastorage.inventory;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientResponseDto;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.inventory.dtos.Inventory.InventorySnapshotCreateDto;
import leoric.pizzacipollastorage.inventory.dtos.Inventory.InventorySnapshotResponseDto;
import leoric.pizzacipollastorage.inventory.models.InventorySnapshot;
import leoric.pizzacipollastorage.inventory.repositories.InventorySnapshotRepository;
import leoric.pizzacipollastorage.inventory.services.InventoryServiceImpl;
import leoric.pizzacipollastorage.inventory.services.InventoryStatusHelperService;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.enums.IngredientState;
import leoric.pizzacipollastorage.models.enums.SnapshotType;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InventoryServiceImplTest {
    @Mock
    private InventorySnapshotRepository inventorySnapshotRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private InventorySnapshotMapper inventorySnapshotMapper;

    @Mock
    private InventoryStatusHelperService inventoryStatusHelperService;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private UUID branchId;
    private UUID ingredientId;
    private Ingredient ingredient;
    private Branch branch;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        branchId = UUID.randomUUID();
        ingredientId = UUID.randomUUID();
        branch = Branch.builder().id(branchId).name("Branch1").build();
        ingredient = Ingredient.builder()
                .id(ingredientId)
                .name("Tomato")
                .branch(branch)
                .lossCleaningFactor(0.1f)
                .build();
    }

    @Test
    void addToInventory_shouldCreateSnapshot() {
        InventorySnapshotResponseDto currentDto = InventorySnapshotResponseDto.builder()
                .id(ingredientId)
                .ingredient(IngredientResponseDto.builder()
                        .id(ingredientId)
                        .name("Tomato")
                        .unit("kg")
                        .lossCleaningFactor(0f)
                        .lossUsageFactor(0f)
                        .productCategory(null)
                        .preferredFullStockLevel(null)
                        .warningStockLevel(null)
                        .minimumStockLevel(null)
                        .build())
                .expectedQuantity(10f)
                .measuredQuantity(10f)
                .lastDiscrepancy(0f)
                .type(SnapshotType.STOCK_IN)
                .note(null)
                .timestamp(LocalDateTime.now())
                .build();

        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(inventoryStatusHelperService.getCurrentInventoryStatusMap(branchId))
                .thenReturn(Map.of(ingredientId, currentDto));

        inventoryService.addToInventory(branchId, ingredientId, 5f);

        ArgumentCaptor<InventorySnapshot> captor = ArgumentCaptor.forClass(InventorySnapshot.class);
        verify(inventorySnapshotRepository).save(captor.capture());
        InventorySnapshot saved = captor.getValue();

        assertEquals(branch, saved.getBranch());
        assertEquals(ingredient, saved.getIngredient());
        assertEquals(15f, saved.getExpectedQuantity());
        assertEquals(15f, saved.getMeasuredQuantity());
        assertEquals(SnapshotType.STOCK_IN, saved.getType());
        assertEquals("Stock received", saved.getNote());
    }

    @Test
    void createSnapshot_shouldCalculateDiscrepancyAndReturnDto() {
        InventorySnapshot lastSnapshot = InventorySnapshot.builder()
                .ingredient(ingredient)
                .branch(branch)
                .measuredQuantity(10f)
                .expectedQuantity(10f)
                .timestamp(LocalDateTime.now().minusDays(1))
                .build();

        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(inventorySnapshotRepository.findTopByIngredientAndBranchOrderByTimestampDesc(ingredient, branch))
                .thenReturn(Optional.of(lastSnapshot));
        IngredientResponseDto ingredientDto = IngredientResponseDto.builder()
                .id(ingredientId)
                .name("Tomato")
                .unit("kg")
                .lossCleaningFactor(0f)
                .lossUsageFactor(0f)
                .productCategory(null)
                .preferredFullStockLevel(null)
                .warningStockLevel(null)
                .minimumStockLevel(null)
                .build();
        InventorySnapshot snapshotSaved = InventorySnapshot.builder().id(UUID.randomUUID()).build();
        when(inventorySnapshotRepository.save(any())).thenReturn(snapshotSaved);
        when(inventorySnapshotMapper.toDto(snapshotSaved)).thenReturn(
                InventorySnapshotResponseDto.builder()
                        .id(ingredientId)
                        .ingredient(ingredientDto)
                        .expectedQuantity(15f)
                        .measuredQuantity(15f)
                        .lastDiscrepancy(5f)
                        .type(SnapshotType.INVENTORY)
                        .note(null)
                        .build()
        );

        InventorySnapshotCreateDto dto = InventorySnapshotCreateDto.builder()
                .ingredientId(ingredientId)
                .measuredQuantity(15f)
                .form(IngredientState.RAW)
                .build();

        InventorySnapshotResponseDto result = inventoryService.createSnapshot(branchId, dto);

        assertNotNull(result);
        assertEquals(5f, result.getLastDiscrepancy());
        verify(inventorySnapshotRepository).save(any());
        verify(inventorySnapshotMapper).toDto(any());
    }

    @Test
    void createSnapshotBulk_shouldSkipInvalidAndReturnResults() {
        InventorySnapshotCreateDto dto1 = InventorySnapshotCreateDto.builder()
                .ingredientId(ingredientId)
                .measuredQuantity(20f)
                .build();

        InventorySnapshotCreateDto dto2 = InventorySnapshotCreateDto.builder()
                .ingredientId(null)
                .measuredQuantity(30f)
                .build();

        IngredientResponseDto ingredientDto = IngredientResponseDto.builder()
                .id(ingredientId)
                .name("Tomato")
                .unit("kg")
                .lossCleaningFactor(0f)
                .lossUsageFactor(0f)
                .productCategory(null)
                .preferredFullStockLevel(null)
                .warningStockLevel(null)
                .minimumStockLevel(null)
                .build();

        InventorySnapshot lastSnapshot = InventorySnapshot.builder()
                .ingredient(ingredient)
                .branch(branch)
                .measuredQuantity(10f)
                .expectedQuantity(10f)
                .timestamp(LocalDateTime.now().minusDays(1))
                .build();

        InventorySnapshotResponseDto responseDto = InventorySnapshotResponseDto.builder()
                .id(ingredientId)
                .ingredient(ingredientDto)
                .expectedQuantity(20f)
                .measuredQuantity(20f)
                .lastDiscrepancy(0f)
                .type(SnapshotType.INVENTORY)
                .note(null)
                .build();

        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(inventorySnapshotRepository.findTopByIngredientAndBranchOrderByTimestampDesc(ingredient, branch))
                .thenReturn(Optional.empty());
        when(inventorySnapshotRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(inventorySnapshotMapper.toDto(any())).thenReturn(responseDto);

        List<InventorySnapshotResponseDto> results = inventoryService.createSnapshotBulk(branchId, List.of(dto1, dto2));

        assertEquals(1, results.size());
        assertEquals(responseDto, results.get(0));
    }
}