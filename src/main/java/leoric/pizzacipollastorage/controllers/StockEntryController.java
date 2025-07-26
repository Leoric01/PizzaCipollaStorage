package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.services.interfaces.StockEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock-entries")
@RequiredArgsConstructor
public class StockEntryController {
    private final StockEntryService stockEntryService;

//    @PostMapping
//    public ResponseEntity<StockEntryResponseDto> create(@RequestBody StockEntryCreateDto dto) {
//        return ResponseEntity.ok(stockEntryService.createStockEntry(dto));
//    }
}