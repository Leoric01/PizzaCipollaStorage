package leoric.pizzacipollastorage.vat.services;

import leoric.pizzacipollastorage.DTOs.ProductCategory.ProductCategoryCreateDto;
import leoric.pizzacipollastorage.DTOs.ProductCategory.ProductCategoryResponseDto;

import java.util.List;
import java.util.UUID;

public interface ProductCategoryService {

    ProductCategoryResponseDto addProductCategory(UUID branchId, ProductCategoryCreateDto dto);

    List<ProductCategoryResponseDto> getAllCategories(UUID branchId);

    ProductCategoryResponseDto getProductCategoryById(UUID id);
}