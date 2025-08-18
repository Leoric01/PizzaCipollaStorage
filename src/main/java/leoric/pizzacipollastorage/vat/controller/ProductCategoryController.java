package leoric.pizzacipollastorage.vat.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchServiceAccess;
import leoric.pizzacipollastorage.vat.dtos.ProductCategory.ProductCategoryCreateBulkDto;
import leoric.pizzacipollastorage.vat.dtos.ProductCategory.ProductCategoryCreateDto;
import leoric.pizzacipollastorage.vat.dtos.ProductCategory.ProductCategoryResponseDto;
import leoric.pizzacipollastorage.vat.services.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/product-categories")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;
    private final BranchServiceAccess branchServiceAccess;

    @PostMapping("/{branchId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<ProductCategoryResponseDto> productCategoryCreate(
            @PathVariable UUID branchId,
            @RequestBody ProductCategoryCreateDto dto,
            @AuthenticationPrincipal User currentUser) {

        branchServiceAccess.assertHasAccess(branchId, currentUser);

        ProductCategoryResponseDto created = productCategoryService.addProductCategory(branchId, dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{branchId}/bulk")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<List<ProductCategoryResponseDto>> productCategoryCreateBulk(
            @PathVariable UUID branchId,
            @RequestBody @Valid ProductCategoryCreateBulkDto bulkDto,
            @AuthenticationPrincipal User currentUser) {

        branchServiceAccess.assertHasAccess(branchId, currentUser);

        List<ProductCategoryResponseDto> created = productCategoryService.bulkAddProductCategories(
                branchId,
                bulkDto.getCategories(),
                currentUser
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{branchId}/all")
    public ResponseEntity<Page<ProductCategoryResponseDto>> productCategoryGetAll(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) String search,
            @ParameterObject
            @Parameter(required = false) Pageable pageable
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);
        return ResponseEntity.ok(productCategoryService.getAllCategories(branchId, search, pageable));
    }

    @GetMapping("/{branchId}/{productCategoryId}")
    public ResponseEntity<ProductCategoryResponseDto> productCategoryGetById(@PathVariable UUID productCategoryId,
                                                                             @PathVariable UUID branchId,
                                                                             @AuthenticationPrincipal User currentUser) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);

        return ResponseEntity.ok(productCategoryService.getProductCategoryById(productCategoryId));
    }

    @PutMapping("/{branchId}/{productCategoryId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<ProductCategoryResponseDto> productCategoryUpdateById(
            @PathVariable UUID branchId,
            @PathVariable UUID productCategoryId,
            @RequestBody @Valid ProductCategoryCreateDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);

        return ResponseEntity.ok(productCategoryService.editProductCategory(productCategoryId, dto, currentUser));
    }

    @DeleteMapping("/{branchId}/{productCategoryId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<Void> productCategoryDelete(
            @PathVariable UUID branchId,
            @PathVariable UUID productCategoryId,
            @AuthenticationPrincipal User currentUser
    ) {
        branchServiceAccess.assertHasAccess(branchId, currentUser);

        productCategoryService.deleteProductCategory(productCategoryId, currentUser);
        return ResponseEntity.noContent().build();
    }
}