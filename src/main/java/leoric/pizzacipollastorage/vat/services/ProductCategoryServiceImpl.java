package leoric.pizzacipollastorage.vat.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.ProductCategory.ProductCategoryCreateDto;
import leoric.pizzacipollastorage.DTOs.ProductCategory.ProductCategoryResponseDto;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.handler.BusinessErrorCodes;
import leoric.pizzacipollastorage.handler.exceptions.BusinessException;
import leoric.pizzacipollastorage.mapstruct.IngredientMapper;
import leoric.pizzacipollastorage.vat.dtos.VatRateShortDto;
import leoric.pizzacipollastorage.vat.models.ProductCategory;
import leoric.pizzacipollastorage.vat.models.VatRate;
import leoric.pizzacipollastorage.vat.repositories.ProductCategoryRepository;
import leoric.pizzacipollastorage.vat.repositories.VatRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Repository
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final BranchRepository branchRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final VatRateRepository vatRateRepository;
    private final IngredientMapper ingredientMapper;

    @Override
    public ProductCategoryResponseDto addProductCategory(UUID branchId, ProductCategoryCreateDto dto) {
        String normalizedName = dto.getName().trim().toUpperCase();

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        productCategoryRepository.findByNameIgnoreCaseAndBranchId(normalizedName, branchId)
                .ifPresent(existing -> {
                    throw new BusinessException(BusinessErrorCodes.CATEGORY_ALREADY_EXISTS);
                });

        VatRate vatRate = vatRateRepository.findById(dto.getVatId())
                .orElseThrow(() -> new EntityNotFoundException("DPH sazba nenalezena."));

        ProductCategory category = ProductCategory.builder()
                .name(normalizedName)
                .vatRate(vatRate)
                .branch(branch)
                .build();

        return mapToResponse(productCategoryRepository.save(category));
    }

    @Override
    public List<ProductCategoryResponseDto> getAllCategories(UUID branchId) {
        return productCategoryRepository.findAllByBranchId(branchId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ProductCategoryResponseDto getProductCategoryById(UUID id) {
        ProductCategory category = productCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kategorie nenalezena."));
        return ingredientMapper.toDto(category);
    }

    private ProductCategoryResponseDto mapToResponse(ProductCategory category) {
        return ProductCategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .vatRate(
                        VatRateShortDto.builder()
                                .id(category.getVatRate().getId())
                                .name(category.getVatRate().getName())
                                .rate(category.getVatRate().getRate())
                                .build()
                )
                .build();
    }
}