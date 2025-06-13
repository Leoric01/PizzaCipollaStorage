package leoric.pizzacipollastorage.vat.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.ProductCategory.ProductCategoryCreateDto;
import leoric.pizzacipollastorage.DTOs.ProductCategory.ProductCategoryResponseDto;
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

    private final ProductCategoryRepository productCategoryRepository;
    private final VatRateRepository vatRateRepository;
    private final IngredientMapper ingredientMapper;

    @Override
    public ProductCategoryResponseDto addProductCategory(ProductCategoryCreateDto dto) {
        String normalizedName = dto.getName().trim().toUpperCase();

        productCategoryRepository.findByNameIgnoreCase(normalizedName).ifPresent(existing -> {
            throw new IllegalArgumentException("Kategorie s názvem '" + dto.getName() + "' již existuje.");
        });

        UUID vatRateId = dto.getVatRate().getId();
        VatRate vatRate = vatRateRepository.findById(vatRateId)
                .orElseThrow(() -> new EntityNotFoundException("DPH sazba s ID " + vatRateId + " nenalezena."));

        ProductCategory category = ProductCategory.builder()
                .name(normalizedName)
                .vatRate(vatRate)
                .build();

        ProductCategory saved = productCategoryRepository.save(category);
        return mapToResponse(saved);
    }

    @Override
    public List<ProductCategoryResponseDto> getAllCategories() {
        return productCategoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ProductCategoryResponseDto getProductCategoryById(UUID id) {
        ProductCategory productCategory = productCategoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("DPH sazba s ID " + id + " nenalezena."));
        return ingredientMapper.toDto(productCategory);
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