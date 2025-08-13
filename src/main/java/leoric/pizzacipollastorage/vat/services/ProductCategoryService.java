package leoric.pizzacipollastorage.vat.services;

import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.vat.dtos.ProductCategory.ProductCategoryCreateDto;
import leoric.pizzacipollastorage.vat.dtos.ProductCategory.ProductCategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductCategoryService {

    ProductCategoryResponseDto addProductCategory(UUID branchId, ProductCategoryCreateDto dto, User currentUser);

    Page<ProductCategoryResponseDto> getAllCategories(UUID branchId, String search, Pageable pageable);

    ProductCategoryResponseDto getProductCategoryById(UUID id);

    List<ProductCategoryResponseDto> bulkAddProductCategories(UUID branchId, List<ProductCategoryCreateDto> categories, User currentUser);

    ProductCategoryResponseDto editProductCategory(UUID id, ProductCategoryCreateDto dto, User currentUser);

    void deleteProductCategory(UUID id, User currentUser);
}