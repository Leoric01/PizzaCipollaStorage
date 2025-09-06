package leoric.pizzacipollastorage.inventory;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.StockEntry.StockEntryCreateDto;
import leoric.pizzacipollastorage.DTOs.StockEntry.StockEntryResponseDto;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.handler.exceptions.NotAuthorizedForBranchException;
import leoric.pizzacipollastorage.inventory.services.InventoryService;
import leoric.pizzacipollastorage.mapstruct.StockEntryMapper;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.StockEntry;
import leoric.pizzacipollastorage.purchase.models.Supplier;
import leoric.pizzacipollastorage.purchase.repositories.SupplierRepository;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.repositories.StockEntryRepository;
import leoric.pizzacipollastorage.services.StockEntryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StockEntryServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private StockEntryRepository stockEntryRepository;

    @Mock
    private StockEntryMapper stockEntryMapper;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private StockEntryServiceImpl stockEntryService;

    private UUID branchId;
    private UUID ingredientId;
    private UUID supplierId;
    private Branch branch;
    private Ingredient ingredient;
    private Supplier supplier;
    private UUID ingredientId1;
    private UUID ingredientId2;
    private UUID supplierId1;
    private UUID supplierId2;
    private Ingredient ingredient1;
    private Ingredient ingredient2;
    private Supplier supplier1;
    private Supplier supplier2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        branchId = UUID.randomUUID();
        ingredientId = UUID.randomUUID();
        supplierId = UUID.randomUUID();

        branch = Branch.builder().id(branchId).name("Branch1").build();
        ingredient = Ingredient.builder().id(ingredientId).name("Tomato").branch(branch).build();
        supplier = Supplier.builder().id(supplierId).name("Supplier1").branch(branch).build();

        ingredientId1 = UUID.randomUUID();
        ingredientId2 = UUID.randomUUID();
        supplierId1 = UUID.randomUUID();
        supplierId2 = UUID.randomUUID();

        branch = Branch.builder().id(branchId).name("Branch1").build();

        ingredient1 = Ingredient.builder().id(ingredientId1).name("Tomato").branch(branch).build();
        ingredient2 = Ingredient.builder().id(ingredientId2).name("Potato").branch(branch).build();

        supplier1 = Supplier.builder().id(supplierId1).name("Supplier1").branch(branch).build();
        supplier2 = Supplier.builder().id(supplierId2).name("Supplier2").branch(branch).build();
    }

    @Test
    void createStockEntries_shouldSaveAllAndCallInventory() {
        StockEntryCreateDto dto1 = new StockEntryCreateDto();
        dto1.setIngredientId(ingredientId1);
        dto1.setSupplierId(supplierId1);
        dto1.setQuantityReceived(10f);

        StockEntryCreateDto dto2 = new StockEntryCreateDto();
        dto2.setIngredientId(ingredientId2);
        dto2.setSupplierId(supplierId2);
        dto2.setQuantityReceived(5f);

        List<StockEntryCreateDto> dtos = List.of(dto1, dto2);

        when(ingredientRepository.findAllById(List.of(ingredientId1, ingredientId2)))
                .thenReturn(List.of(ingredient1, ingredient2));
        when(supplierRepository.findAllById(List.of(supplierId1, supplierId2)))
                .thenReturn(List.of(supplier1, supplier2));

        StockEntry entry1 = StockEntry.builder().build();
        StockEntry entry2 = StockEntry.builder().build();

        when(stockEntryMapper.toEntity(dto1)).thenReturn(entry1);
        when(stockEntryMapper.toEntity(dto2)).thenReturn(entry2);

        StockEntry saved1 = StockEntry.builder().id(UUID.randomUUID()).build();
        StockEntry saved2 = StockEntry.builder().id(UUID.randomUUID()).build();

        when(stockEntryRepository.saveAll(List.of(entry1, entry2))).thenReturn(List.of(saved1, saved2));
        when(stockEntryMapper.toDto(saved1)).thenReturn(new StockEntryResponseDto());
        when(stockEntryMapper.toDto(saved2)).thenReturn(new StockEntryResponseDto());

        List<StockEntryResponseDto> results = stockEntryService.createStockEntries(branchId, dtos);

        assertEquals(2, results.size());
        verify(stockEntryRepository).saveAll(List.of(entry1, entry2));
        verify(inventoryService).addToInventory(branchId, ingredientId1, 10f);
        verify(inventoryService).addToInventory(branchId, ingredientId2, 5f);
    }

    @Test
    void createStockEntries_shouldThrowIfIngredientNotFound() {
        StockEntryCreateDto dto = new StockEntryCreateDto();
        dto.setIngredientId(ingredientId1);
        dto.setSupplierId(supplierId1);

        when(ingredientRepository.findAllById(List.of(ingredientId1))).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class,
                () -> stockEntryService.createStockEntries(branchId, List.of(dto)));
    }

    @Test
    void createStockEntries_shouldThrowIfIngredientWrongBranch() {
        StockEntryCreateDto dto = new StockEntryCreateDto();
        dto.setIngredientId(ingredientId1);
        dto.setSupplierId(supplierId1);

        Branch otherBranch = Branch.builder().id(UUID.randomUUID()).build();
        ingredient1.setBranch(otherBranch);

        when(ingredientRepository.findAllById(List.of(ingredientId1))).thenReturn(List.of(ingredient1));

        assertThrows(NotAuthorizedForBranchException.class,
                () -> stockEntryService.createStockEntries(branchId, List.of(dto)));
    }

    @Test
    void createStockEntries_shouldThrowIfSupplierNotFound() {
        StockEntryCreateDto dto = new StockEntryCreateDto();
        dto.setIngredientId(ingredientId1);
        dto.setSupplierId(supplierId1);

        when(ingredientRepository.findAllById(List.of(ingredientId1))).thenReturn(List.of(ingredient1));
        when(supplierRepository.findAllById(List.of(supplierId1))).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class,
                () -> stockEntryService.createStockEntries(branchId, List.of(dto)));
    }

    @Test
    void stockEntryCreate_shouldCreateEntryAndCallInventory() {
        StockEntryCreateDto dto = new StockEntryCreateDto();
        dto.setIngredientId(ingredientId);
        dto.setSupplierId(supplierId);
        dto.setQuantityReceived(10f);
        dto.setPricePerUnitWithoutTax(5f);
        dto.setReceivedDate(LocalDate.now());

        StockEntry entryToSave = StockEntry.builder().build();
        StockEntry savedEntry = StockEntry.builder().id(UUID.randomUUID()).build();

        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(supplier));
        when(stockEntryMapper.toEntity(dto)).thenReturn(entryToSave);
        when(stockEntryRepository.save(entryToSave)).thenReturn(savedEntry);
        when(stockEntryMapper.toDto(savedEntry)).thenReturn(new StockEntryResponseDto());

        StockEntryResponseDto result = stockEntryService.stockEntryCreate(branchId, dto);

        assertNotNull(result);
        verify(stockEntryRepository).save(entryToSave);
        verify(inventoryService).addToInventory(branchId, ingredientId, 10f);
    }

    @Test
    void stockEntryCreate_shouldThrowIfIngredientNotFound() {
        StockEntryCreateDto dto = new StockEntryCreateDto();
        dto.setIngredientId(ingredientId);

        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> stockEntryService.stockEntryCreate(branchId, dto));
    }

    @Test
    void stockEntryCreate_shouldThrowIfIngredientInWrongBranch() {
        StockEntryCreateDto dto = new StockEntryCreateDto();
        dto.setIngredientId(ingredientId);

        Branch otherBranch = Branch.builder().id(UUID.randomUUID()).build();
        ingredient.setBranch(otherBranch);

        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(ingredient));

        assertThrows(NotAuthorizedForBranchException.class,
                () -> stockEntryService.stockEntryCreate(branchId, dto));
    }

    @Test
    void stockEntryCreate_shouldThrowIfSupplierNotFound() {
        StockEntryCreateDto dto = new StockEntryCreateDto();
        dto.setIngredientId(ingredientId);
        dto.setSupplierId(supplierId);

        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(supplierRepository.findById(supplierId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> stockEntryService.stockEntryCreate(branchId, dto));
    }
}