package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleLastTimestampCreateDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleLastTimestampResponseDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleLastTimestampUpdateDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface MenuItemSaleLastTimestampService {
    MenuItemSaleLastTimestampResponseDto saleTimestampGetByBranch(UUID branchId);

    MenuItemSaleLastTimestampResponseDto saleTimestampCreate(UUID branchId, MenuItemSaleLastTimestampCreateDto dto);

    MenuItemSaleLastTimestampResponseDto saleTimestampUpdate(UUID branchId, MenuItemSaleLastTimestampUpdateDto dto);
}