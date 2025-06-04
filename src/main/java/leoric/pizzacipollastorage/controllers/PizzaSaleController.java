package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.PizzaSale.PizzaSaleCreateDto;
import leoric.pizzacipollastorage.DTOs.PizzaSale.PizzaSaleResponseDto;
import leoric.pizzacipollastorage.services.interfaces.PizzaSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pizza-sales")
@RequiredArgsConstructor
public class PizzaSaleController {

    private final PizzaSaleService pizzaSaleService;

    @PostMapping
    public ResponseEntity<PizzaSaleResponseDto> createSale(@RequestBody PizzaSaleCreateDto dto) {
        PizzaSaleResponseDto response = pizzaSaleService.createSale(dto);
        return ResponseEntity.ok(response);
    }
}