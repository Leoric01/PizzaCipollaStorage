package leoric.pizzacipollastorage.init;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientCreateDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientResponseDto;
import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemCategoryCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemCategoryResponseDto;
import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemFullCreateDto;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.init.dto.*;
import leoric.pizzacipollastorage.inventory.services.InventoryService;
import leoric.pizzacipollastorage.purchase.dtos.PurchaseInvoice.PurchaseInvoiceCreateDto;
import leoric.pizzacipollastorage.purchase.dtos.PurchaseInvoice.PurchaseInvoiceItemCreateDto;
import leoric.pizzacipollastorage.purchase.dtos.Supplier.SupplierCreateDto;
import leoric.pizzacipollastorage.purchase.services.PurchaseInvoiceService;
import leoric.pizzacipollastorage.purchase.services.SupplierService;
import leoric.pizzacipollastorage.services.interfaces.IngredientService;
import leoric.pizzacipollastorage.services.interfaces.MenuItemCategoryService;
import leoric.pizzacipollastorage.services.interfaces.MenuItemService;
import leoric.pizzacipollastorage.vat.dtos.ProductCategory.ProductCategoryCreateDto;
import leoric.pizzacipollastorage.vat.dtos.ProductCategory.ProductCategoryResponseDto;
import leoric.pizzacipollastorage.vat.services.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BranchBootstrapServiceImpl implements BranchBootstrapService {

    private final ObjectMapper objectMapper;
    private final ProductCategoryService productCategoryService;
    private final IngredientService ingredientService;
    private final MenuItemService menuItemService;
    private final MenuItemCategoryService menuItemCategoryService;
    private final SupplierService supplierService;
    private final InventoryService inventoryService;
    private final PurchaseInvoiceService purchaseInvoiceService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void bootstrapBranch(UUID branchId, User currentUser) {
        BranchDefaultDataDto defaultData = loadDefaultData();

        Map<String, UUID> categoryNameToId = new HashMap<>();
        Map<String, UUID> ingredientNameToId = new HashMap<>();

        // 1. Product Categories (Ponecháno pro případné budoucí použití)
        if (defaultData.getProductCategories() != null && !defaultData.getProductCategories().isEmpty()) {
            List<ProductCategoryResponseDto> created = productCategoryService.bulkAddProductCategories(
                    branchId,
                    defaultData.getProductCategories(),
                    currentUser
            );
            created.forEach(cat -> categoryNameToId.put(cat.getName().trim().toUpperCase(), cat.getId()));
        }

        // 1.5. MenuItem Categories (NOVÝ KROK)
        Map<String, UUID> menuItemCategoryNameToId = new HashMap<>();
        if (defaultData.getMenuItemCategories() != null && !defaultData.getMenuItemCategories().isEmpty()) {
            List<MenuItemCategoryResponseDto> created = menuItemCategoryService.menuItemCategoryAddBulk(
                    branchId,
                    defaultData.getMenuItemCategories()
            );
            created.forEach(cat -> menuItemCategoryNameToId.put(cat.getName().trim().toUpperCase(), cat.getId()));
        }

        // 2. Ingredients
        if (defaultData.getIngredients() != null) {
            List<IngredientCreateDto> ingredientDtos = defaultData.getIngredients().stream()
                    .map(boot -> new IngredientCreateDto(
                            boot.getName(),
                            boot.getUnit(),
                            boot.getLossCleaningFactor(),
                            boot.getLossUsageFactor(),
                            categoryNameToId.get(boot.getProductCategoryName().trim().toUpperCase()),
                            boot.getPreferredFullStockLevel(),
                            boot.getWarningStockLevel(),
                            boot.getMinimumStockLevel()
                    ))
                    .toList();

            List<IngredientResponseDto> createdIngredients = ingredientService.ingredientCreateBulk(branchId, ingredientDtos);
            createdIngredients.forEach(ing -> ingredientNameToId.put(ing.name().trim().toUpperCase(), ing.id()));
        }

        // 3. Menu Items
        if (defaultData.getMenuItems() != null && !defaultData.getMenuItems().isEmpty()) {
            List<MenuItemFullCreateDto> menuItemDtos = defaultData.getMenuItems().stream().map(mi -> {
                // 3a. Kategorie menu itemu
                UUID categoryId = null;
                if (mi.getMenuItemCategory() != null && mi.getMenuItemCategory().getName() != null) {
                    String categoryNameKey = mi.getMenuItemCategory().getName().trim().toUpperCase();
                    // Není třeba ověřovat existenci a vytvářet, protože už jsme je vytvořili v kroku 1.5
                    categoryId = menuItemCategoryNameToId.get(categoryNameKey);
                }

                // 3b. Ingredience
                List<MenuItemFullCreateDto.RecipeIngredientSimpleDto> ingredients = mi.getIngredients() != null
                        ? mi.getIngredients().stream()
                        .map(ri -> MenuItemFullCreateDto.RecipeIngredientSimpleDto.builder()
                                .ingredientId(ingredientNameToId.get(ri.getIngredientName().trim().toUpperCase()))
                                .quantity(ri.getQuantity())
                                .build())
                        .toList()
                        : Collections.emptyList();

                // 3c. Sestavení DTO
                return MenuItemFullCreateDto.builder()
                        .name(mi.getName())
                        .description(mi.getDescription())
                        .size(mi.getSize())
                        .menuItemCategoryId(categoryId)
                        .ingredients(ingredients)
                        .build();
            }).toList();

            menuItemService.createMenuItemsBulk(branchId, menuItemDtos);
        }

        // 4. Suppliers
        if (defaultData.getSuppliers() != null && !defaultData.getSuppliers().isEmpty()) {
            List<SupplierCreateDto> supplierDtos = defaultData.getSuppliers().stream()
                    .map(s -> {
                        SupplierCreateDto dto = new SupplierCreateDto();
                        dto.setName(s.getName());
                        dto.setContactInfo(s.getContactInfo());
                        return dto;
                    })
                    .toList();

            supplierService.supplierCreateBulk(branchId, supplierDtos);
        }

        // 5. Inventory Snapshots
//        if (defaultData.getInventorySnapshots() != null && !defaultData.getInventorySnapshots().isEmpty()) {
//            List<InventorySnapshotCreateDto> snapshotDtos = defaultData.getInventorySnapshots().stream()
//                    .map(snap -> {
//                        InventorySnapshotCreateDto dto = new InventorySnapshotCreateDto();
//                        dto.setIngredientId(ingredientNameToId.get(snap.getIngredientName().trim().toUpperCase()));
//                        dto.setMeasuredQuantity(snap.getMeasuredQuantity());
//                        dto.setNote(snap.getNote());
//                        dto.setForm(snap.getForm() != null ? snap.getForm() : IngredientState.RAW);
//                        return dto;
//                    })
//                    .filter(dto -> dto.getIngredientId() != null) // jen existující ingredience
//                    .toList();
//
//            if (!snapshotDtos.isEmpty()) {
//                inventoryService.createSnapshotBulk(branchId, snapshotDtos);
//            }
//        }
        // 6. Purchase Invoices (naskladnění)
        if (defaultData.getPurchaseInvoices() != null && !defaultData.getPurchaseInvoices().isEmpty()) {
            List<PurchaseInvoiceCreateDto> invoiceDtos = defaultData.getPurchaseInvoices().stream()
                    .map(inv -> {
                        PurchaseInvoiceCreateDto dto = new PurchaseInvoiceCreateDto();
                        dto.setInvoiceNumber(inv.getInvoiceNumber());
                        dto.setSupplierName(inv.getSupplierName());
                        dto.setIssuedDate(inv.getIssuedDate());
                        dto.setReceivedDate(inv.getReceivedDate());
                        dto.setNote(inv.getNote());

                        List<PurchaseInvoiceItemCreateDto> items = inv.getItems().stream()
                                .map(item -> {
                                    PurchaseInvoiceItemCreateDto iDto = new PurchaseInvoiceItemCreateDto();
                                    iDto.setIngredientName(item.getIngredientName());
                                    iDto.setQuantity(item.getQuantity());
                                    iDto.setUnitPriceWithoutTax(item.getUnitPriceWithoutTax());
                                    return iDto;
                                })
                                .toList();

                        dto.setItems(items);
                        return dto;
                    })
                    .toList();

            purchaseInvoiceService.createInvoicesBulk(branchId, invoiceDtos, currentUser);
        }
    }

    private BranchDefaultDataDto loadDefaultData() {
        try {
            // Create an instance of the DTO
            BranchDefaultDataDto.BranchDefaultDataDtoBuilder builder = BranchDefaultDataDto.builder();

            // Load Product Categories from the new file
            try (InputStream is = getClass().getResourceAsStream("/data/product-categories.json")) {
                List<ProductCategoryCreateDto> productCategories = objectMapper.readValue(is, new TypeReference<>() {
                });
                builder.productCategories(productCategories);
            }

            try (InputStream is = getClass().getResourceAsStream("/data/menu-item-categories.json")) {
                List<MenuItemCategoryCreateDto> menuItemCategories = objectMapper.readValue(is, new TypeReference<>() {
                });
                builder.menuItemCategories(menuItemCategories);
            }

            // Load Ingredients from the new file
            try (InputStream is = getClass().getResourceAsStream("/data/ingredients.json")) {
                List<IngredientBootstrapDto> ingredients = objectMapper.readValue(is, new TypeReference<>() {
                });
                builder.ingredients(ingredients);
            }

            // Load Menu Items from the new file
            try (InputStream is = getClass().getResourceAsStream("/data/menu-items.json")) {
                List<MenuItemBootstrapDto> menuItems = objectMapper.readValue(is, new TypeReference<>() {
                });
                builder.menuItems(menuItems);
            }

            // Load Suppliers from the new file
            try (InputStream is = getClass().getResourceAsStream("/data/suppliers.json")) {
                List<SupplierBootstrapDto> suppliers = objectMapper.readValue(is, new TypeReference<>() {
                });
                builder.suppliers(suppliers);
            }

            // Load Purchase Invoices from the new file
            try (InputStream is = getClass().getResourceAsStream("/data/purchase-invoice.json")) {
                List<PurchaseInvoiceBootstrapDto> purchaseInvoices = objectMapper.readValue(is, new TypeReference<>() {
                });
                builder.purchaseInvoices(purchaseInvoices);
            }

            // Build and return the final DTO object
            return builder.build();

        } catch (IOException e) {
            throw new IllegalStateException("Nepodařilo se načíst výchozí data pro pobočku z rozdělených souborů", e);
        }
    }
}