package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.services.interfaces.MenuItemSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menuitems-sales")
@RequiredArgsConstructor
public class MenuItemSaleController {

    private final MenuItemSaleService menuItemSaleService;

//    @PostMapping
//    public ResponseEntity<MenuItemSaleResponseDto> createSale(@RequestBody MenuItemSaleCreateDto dto) {
//        MenuItemSaleResponseDto response = menuItemSaleService.createSale(dto);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/bulk")
//    public ResponseEntity<List<MenuItemSaleResponseDto>> createSaleBulk(@RequestBody List<MenuItemSaleCreateDto> dtos) {
//        List<MenuItemSaleResponseDto> responses = menuItemSaleService.createSaleBulk(dtos);
//        return ResponseEntity.ok(responses);
//    }
}