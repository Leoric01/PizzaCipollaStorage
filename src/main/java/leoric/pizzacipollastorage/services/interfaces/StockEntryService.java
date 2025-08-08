package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.StockEntry.StockEntryCreateDto;
import leoric.pizzacipollastorage.DTOs.StockEntry.StockEntryResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface StockEntryService {
    StockEntryResponseDto stockEntryCreate(UUID branchId, StockEntryCreateDto dto);

    List<StockEntryResponseDto> createStockEntries(UUID branchId, List<StockEntryCreateDto> dtos);
}