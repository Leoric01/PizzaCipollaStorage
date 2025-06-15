package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.DishSize.DishSizeCreateDto;
import leoric.pizzacipollastorage.DTOs.DishSize.DishSizeResponseDto;
import leoric.pizzacipollastorage.services.interfaces.DishSizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @PutMapping("/{id}")
    public ResponseEntity<DishSizeResponseDto> update(@PathVariable UUID id, @RequestBody DishSizeCreateDto dto) {
        return ResponseEntity.ok(dishSizeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        dishSizeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}