package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface MenuItemSaleService {
    MenuItemSaleResponseDto createSale(UUID branchId, MenuItemSaleCreateDto dto);

    List<MenuItemSaleResponseDto> createSaleBulk(UUID branchId, List<MenuItemSaleCreateDto> dtos);
}