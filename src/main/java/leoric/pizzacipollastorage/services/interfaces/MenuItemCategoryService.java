package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemCategoryCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItem.MenuItemCategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface MenuItemCategoryService {
    Page<MenuItemCategoryResponseDto> menuItemCategoryGetAll(UUID branchId, String search, Pageable pageable);

    MenuItemCategoryResponseDto menuItemCategoryAdd(UUID branchId, MenuItemCategoryCreateDto dto);

    MenuItemCategoryResponseDto menuItemCategoryUpdate(UUID branchId, UUID id, MenuItemCategoryCreateDto dto);

    void menuItemCategoryDelete(UUID branchId, UUID id);

    List<MenuItemCategoryResponseDto> menuItemCategoryAddBulk(UUID branchId, List<MenuItemCategoryCreateDto> dtos);

    MenuItemCategoryResponseDto menuItemCategoryFindById(UUID menuItemCategoryId);
}