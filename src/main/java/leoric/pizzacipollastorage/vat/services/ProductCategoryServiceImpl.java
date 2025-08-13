package leoric.pizzacipollastorage.vat.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchServiceAccess;
import leoric.pizzacipollastorage.handler.BusinessErrorCodes;
import leoric.pizzacipollastorage.handler.exceptions.BusinessException;
import leoric.pizzacipollastorage.handler.exceptions.DuplicateCategoryNameException;
import leoric.pizzacipollastorage.mapstruct.IngredientMapper;
import leoric.pizzacipollastorage.vat.dtos.ProductCategory.ProductCategoryCreateDto;
import leoric.pizzacipollastorage.vat.dtos.ProductCategory.ProductCategoryResponseDto;
import leoric.pizzacipollastorage.vat.dtos.VatRateShortDto;
import leoric.pizzacipollastorage.vat.models.ProductCategory;
import leoric.pizzacipollastorage.vat.models.VatRate;
import leoric.pizzacipollastorage.vat.repositories.ProductCategoryRepository;
import leoric.pizzacipollastorage.vat.repositories.VatRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Repository
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final VatRateRepository vatRateRepository;
    private final IngredientMapper ingredientMapper;
    private final BranchServiceAccess branchServiceAccess;

    @Override
    public ProductCategoryResponseDto addProductCategory(UUID branchId, ProductCategoryCreateDto dto, User currentUser) {
        String normalizedName = dto.getName().trim().toUpperCase();

        Branch branch = branchServiceAccess.verifyAccess(branchId, currentUser);

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
    public List<ProductCategoryResponseDto> bulkAddProductCategories(UUID branchId, List<ProductCategoryCreateDto> dtos, User currentUser) {
        Branch branch = branchServiceAccess.verifyAccess(branchId, currentUser);

        Map<ProductCategoryCreateDto, String> dtoToNormalizedName = dtos.stream()
                .collect(Collectors.toMap(
                        dto -> dto,
                        dto -> dto.getName().trim().toUpperCase()
                ));

        List<String> normalizedNames = new ArrayList<>(dtoToNormalizedName.values());

        List<ProductCategory> existing = productCategoryRepository
                .findAllByNameInIgnoreCaseAndBranchId(normalizedNames, branchId);

        Set<String> existingNames = existing.stream()
                .map(cat -> cat.getName().trim().toUpperCase())
                .collect(Collectors.toSet());

        for (Map.Entry<ProductCategoryCreateDto, String> entry : dtoToNormalizedName.entrySet()) {
            if (existingNames.contains(entry.getValue())) {
                throw new DuplicateCategoryNameException(
                        "Kategorie s názvem '" + entry.getKey().getName() + "' už existuje pro tuto pobočku.");
            }
        }

        Set<UUID> vatIds = dtos.stream()
                .map(ProductCategoryCreateDto::getVatId)
                .collect(Collectors.toSet());

        Map<UUID, VatRate> vatMap = vatRateRepository.findAllById(vatIds).stream()
                .collect(Collectors.toMap(VatRate::getId, vr -> vr));

        for (UUID vatId : vatIds) {
            if (!vatMap.containsKey(vatId)) {
                throw new EntityNotFoundException("DPH sazba nenalezena: " + vatId);
            }
        }

        List<ProductCategory> toSave = dtos.stream()
                .map(dto -> ProductCategory.builder()
                        .name(dto.getName().trim().toUpperCase())
                        .vatRate(vatMap.get(dto.getVatId()))
                        .branch(branch)
                        .build())
                .collect(Collectors.toList());

        return productCategoryRepository.saveAll(toSave).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductCategoryResponseDto editProductCategory(UUID id, ProductCategoryCreateDto dto, User currentUser) {
        ProductCategory category = productCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kategorie nenalezena."));

        String normalizedName = dto.getName().trim().toUpperCase();

        productCategoryRepository.findByNameIgnoreCaseAndBranchId(normalizedName, category.getBranch().getId())
                .filter(existing -> !existing.getId().equals(category.getId()))
                .ifPresent(existing -> {
                    throw new BusinessException(BusinessErrorCodes.CATEGORY_ALREADY_EXISTS);
                });

        VatRate vatRate = vatRateRepository.findById(dto.getVatId())
                .orElseThrow(() -> new EntityNotFoundException("DPH sazba nenalezena."));

        category.setName(normalizedName);
        category.setVatRate(vatRate);

        return mapToResponse(productCategoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteProductCategory(UUID id, User currentUser) {
        ProductCategory category = productCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kategorie nenalezena."));

        productCategoryRepository.delete(category);
    }

    @Override
    public Page<ProductCategoryResponseDto> getAllCategories(UUID branchId, String search, Pageable pageable) {
        Page<ProductCategory> page;
        if (search != null && !search.isBlank()) {
            page = productCategoryRepository.findByBranchIdAndNameContainingIgnoreCase(branchId, search, pageable);
        } else {
            page = productCategoryRepository.findByBranchId(branchId, pageable);
        }
        return page.map(this::mapToResponse);
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