package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.DishSize.DishSizeCreateDto;
import leoric.pizzacipollastorage.DTOs.DishSize.DishSizeResponseDto;
import leoric.pizzacipollastorage.services.interfaces.DishSizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dish-sizes")
@RequiredArgsConstructor
public class DishSizeController {

    private final DishSizeService dishSizeService;

    @PostMapping
    public ResponseEntity<DishSizeResponseDto> create(@RequestBody DishSizeCreateDto dto) {
        return ResponseEntity.ok(dishSizeService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<DishSizeResponseDto>> getAll() {
        return ResponseEntity.ok(dishSizeService.getAll());
    }
}