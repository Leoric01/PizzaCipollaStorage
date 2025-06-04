package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.StockEntry.StockEntryCreateDto;
import leoric.pizzacipollastorage.DTOs.StockEntry.StockEntryResponseDto;
import leoric.pizzacipollastorage.services.interfaces.StockEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock-entries")
@RequiredArgsConstructor
public class StockEntryController {
    private final StockEntryService stockEntryService;

    @PostMapping
    public ResponseEntity<StockEntryResponseDto> create(@RequestBody StockEntryCreateDto dto) {
        return ResponseEntity.ok(stockEntryService.createStockEntry(dto));
    }
}