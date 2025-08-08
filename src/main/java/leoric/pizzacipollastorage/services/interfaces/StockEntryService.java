package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.StockEntry.StockEntryCreateDto;
import leoric.pizzacipollastorage.DTOs.StockEntry.StockEntryResponseDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface StockEntryService {
    StockEntryResponseDto createStockEntry(UUID branchId, StockEntryCreateDto dto);
}