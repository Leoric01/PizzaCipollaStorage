package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.ProductCategory.ProductCategoryCreateDto;
import leoric.pizzacipollastorage.DTOs.ProductCategory.ProductCategoryResponseDto;

import java.util.List;
import java.util.UUID;

public interface ProductCategoryService {

    ProductCategoryResponseDto addProductCategory(ProductCategoryCreateDto dto);

    List<ProductCategoryResponseDto> getAllCategories();

    ProductCategoryResponseDto getProductCategoryById(UUID id);
}