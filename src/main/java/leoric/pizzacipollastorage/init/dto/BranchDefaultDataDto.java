package leoric.pizzacipollastorage.init.dto;

import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemCategoryCreateDto;
import leoric.pizzacipollastorage.vat.dtos.ProductCategory.ProductCategoryCreateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchDefaultDataDto {
    private List<ProductCategoryCreateDto> productCategories;
    private List<IngredientBootstrapDto> ingredients;
    private List<MenuItemCategoryCreateDto> menuItemCategories;
    private List<MenuItemBootstrapDto> menuItems;
    private List<SupplierBootstrapDto> suppliers;
    private List<PurchaseInvoiceBootstrapDto> purchaseInvoices;

}