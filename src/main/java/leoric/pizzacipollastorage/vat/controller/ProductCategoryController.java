package leoric.pizzacipollastorage.vat.controller;

import leoric.pizzacipollastorage.DTOs.ProductCategory.ProductCategoryCreateDto;
import leoric.pizzacipollastorage.DTOs.ProductCategory.ProductCategoryResponseDto;
import leoric.pizzacipollastorage.vat.services.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/product-categories")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @PostMapping("/{branchId}")
    public ResponseEntity<ProductCategoryResponseDto> createCategory(
            @PathVariable UUID branchId,
            @RequestBody ProductCategoryCreateDto dto) {
        ProductCategoryResponseDto created = productCategoryService.addProductCategory(branchId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{branchId}")
    public ResponseEntity<List<ProductCategoryResponseDto>> getAllCategories(@PathVariable UUID branchId) {
        return ResponseEntity.ok(productCategoryService.getAllCategories(branchId));
    }

    @GetMapping
    public ResponseEntity<ProductCategoryResponseDto> getCategoryById(@RequestParam UUID id) {
        return ResponseEntity.ok(productCategoryService.getProductCategoryById(id));
    }
}