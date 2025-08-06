package leoric.pizzacipollastorage.vat.controller;

import jakarta.validation.Valid;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.vat.dtos.ProductCategory.ProductCategoryCreateBulkDto;
import leoric.pizzacipollastorage.vat.dtos.ProductCategory.ProductCategoryCreateDto;
import leoric.pizzacipollastorage.vat.dtos.ProductCategory.ProductCategoryResponseDto;
import leoric.pizzacipollastorage.vat.services.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/product-categories")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @PostMapping("/{branchId}")
    public ResponseEntity<ProductCategoryResponseDto> productCategoryCreate(
            @PathVariable UUID branchId,
            @RequestBody ProductCategoryCreateDto dto,
            @AuthenticationPrincipal User currentUser) {

        ProductCategoryResponseDto created = productCategoryService.addProductCategory(branchId, dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{branchId}/bulk")
    public ResponseEntity<List<ProductCategoryResponseDto>> bulkCreate(
            @PathVariable UUID branchId,
            @RequestBody @Valid ProductCategoryCreateBulkDto bulkDto,
            @AuthenticationPrincipal User currentUser) {

        List<ProductCategoryResponseDto> created = productCategoryService.bulkAddProductCategories(branchId, bulkDto.getCategories(), currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{branchId}")
    public ResponseEntity<List<ProductCategoryResponseDto>> productCategoryGetAll(
            @PathVariable UUID branchId, @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(productCategoryService.getAllCategories(branchId, currentUser));
    }

    @GetMapping
    public ResponseEntity<ProductCategoryResponseDto> productCategoryGetById(@RequestParam UUID id) {
        return ResponseEntity.ok(productCategoryService.getProductCategoryById(id));
    }
}