package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface MenuItemSaleService {
    MenuItemSaleResponseDto createSale(MenuItemSaleCreateDto dto);
}