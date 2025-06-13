package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemCategoryCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemCategoryResponseDto;
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
    private final MenuItemCategoryRepository menuItemCategoryRepository;
    private final MenuItemCategoryMapper menuItemCategoryMapper;

    @Override
    public List<MenuItemCategoryResponseDto> findAll() {
        return menuItemCategoryMapper.toDtoList(menuItemCategoryRepository.findAll());
    }

    @Override
    public MenuItemCategoryResponseDto add(MenuItemCategoryCreateDto dto) {
        String name = dto.getName().trim();
        if (menuItemCategoryRepository.findByNameIgnoreCase(name).isPresent()) {
            throw new IllegalArgumentException("Category with name '" + name + "' already exists.");
        }

        MenuItemCategory newCategory = menuItemCategoryMapper.toEntity(dto);
        newCategory.setName(name);
        return menuItemCategoryMapper.toDto(menuItemCategoryRepository.save(newCategory));
    }

    @Override
    public MenuItemCategoryResponseDto update(UUID id, MenuItemCategoryCreateDto dto) {
        MenuItemCategory category = menuItemCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        String newName = dto.getName().trim();

        // Check duplicity ignoring the current entity itself
        menuItemCategoryRepository.findByNameIgnoreCase(newName)
                .filter(existingCategory -> !existingCategory.getId().equals(id))
                .ifPresent(existingCategory -> {
                    throw new IllegalArgumentException("Category with name '" + newName + "' already exists.");
                });

        menuItemCategoryMapper.update(category, dto);
        category.setName(newName); // ensure trimmed name

        return menuItemCategoryMapper.toDto(menuItemCategoryRepository.save(category));
    }

    @Override
    public void delete(UUID id) {
        MenuItemCategory category = menuItemCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        if (!category.getMenuItems().isEmpty()) {
            throw new IllegalStateException("Cannot delete category while it has menu items assigned.");
        }

        menuItemCategoryRepository.delete(category);
    }
}