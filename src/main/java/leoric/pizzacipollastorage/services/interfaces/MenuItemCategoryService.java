package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemCategoryCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemCategoryResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface MenuItemCategoryService {
    List<MenuItemCategoryResponseDto> findAll(UUID branchId);

    MenuItemCategoryResponseDto add(UUID branchId, MenuItemCategoryCreateDto dto);

    MenuItemCategoryResponseDto update(UUID branchId, UUID id, MenuItemCategoryCreateDto dto);

    void delete(UUID branchId, UUID id);
}