package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemCategoryCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemCategoryResponseDto;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.handler.BusinessErrorCodes;
import leoric.pizzacipollastorage.handler.exceptions.BusinessException;
import leoric.pizzacipollastorage.mapstruct.MenuItemCategoryMapper;
import leoric.pizzacipollastorage.models.MenuItemCategory;
import leoric.pizzacipollastorage.repositories.MenuItemCategoryRepository;
import leoric.pizzacipollastorage.services.interfaces.MenuItemCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuItemCategoryServiceImpl implements MenuItemCategoryService {

    private final BranchRepository branchRepository;
    private final MenuItemCategoryRepository menuItemCategoryRepository;
    private final MenuItemCategoryMapper menuItemCategoryMapper;

    @Override
    public List<MenuItemCategoryResponseDto> menuItemCategoryFindAll(UUID branchId) {
        return menuItemCategoryMapper.toDtoList(
                menuItemCategoryRepository.findAllByBranchId(branchId)
        );
    }

    @Override
    public MenuItemCategoryResponseDto menuItemCategoryAdd(UUID branchId, MenuItemCategoryCreateDto dto) {
        String name = dto.getName().trim();
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        menuItemCategoryRepository.findByNameIgnoreCaseAndBranchId(name, branchId)
                .ifPresent(existing -> {
                    throw new BusinessException(BusinessErrorCodes.MENU_CATEGORY_ALREADY_EXISTS);
                });

        MenuItemCategory newCategory = MenuItemCategory.builder()
                .name(name)
                .branch(branch)
                .build();

        return menuItemCategoryMapper.toDto(menuItemCategoryRepository.save(newCategory));
    }

    @Override
    public List<MenuItemCategoryResponseDto> menuItemCategoryAddBulk(UUID branchId, List<MenuItemCategoryCreateDto> dtos) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        List<MenuItemCategory> toSave = dtos.stream()
                .map(dto -> {
                    String name = dto.getName().trim();
                    menuItemCategoryRepository.findByNameIgnoreCaseAndBranchId(name, branchId)
                            .ifPresent(existing -> {
                                throw new BusinessException(BusinessErrorCodes.MENU_CATEGORY_ALREADY_EXISTS);
                            });

                    return MenuItemCategory.builder()
                            .name(name)
                            .branch(branch)
                            .build();
                })
                .toList();

        List<MenuItemCategory> saved = menuItemCategoryRepository.saveAll(toSave);
        return menuItemCategoryMapper.toDtoList(saved);
    }

    @Override
    public MenuItemCategoryResponseDto menuItemCategoryFindById(UUID menuItemCategoryId) {
        MenuItemCategory category = menuItemCategoryRepository.findById(menuItemCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Menu category not found: " + menuItemCategoryId));

        return menuItemCategoryMapper.toDto(category);
    }

    @Override
    public MenuItemCategoryResponseDto menuItemCategoryUpdate(UUID branchId, UUID id, MenuItemCategoryCreateDto dto) {
        MenuItemCategory category = menuItemCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        if (!category.getBranch().getId().equals(branchId)) {
            throw new BusinessException(BusinessErrorCodes.NOT_AUTHORIZED_FOR_BRANCH);
        }

        String newName = dto.getName().trim();
        menuItemCategoryRepository.findByNameIgnoreCaseAndBranchId(newName, branchId)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BusinessException(BusinessErrorCodes.MENU_CATEGORY_ALREADY_EXISTS);
                });

        category.setName(newName);
        return menuItemCategoryMapper.toDto(menuItemCategoryRepository.save(category));
    }

    @Override
    public void menuItemCategoryDelete(UUID branchId, UUID id) {
        MenuItemCategory category = menuItemCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        if (!category.getBranch().getId().equals(branchId)) {
            throw new BusinessException(BusinessErrorCodes.NOT_AUTHORIZED_FOR_BRANCH);
        }

        if (!category.getMenuItems().isEmpty()) {
            throw new BusinessException(BusinessErrorCodes.MENU_CATEGORY_IN_USE);
        }

        menuItemCategoryRepository.delete(category);
    }
}