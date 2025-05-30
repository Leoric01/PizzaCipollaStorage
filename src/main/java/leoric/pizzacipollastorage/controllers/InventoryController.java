package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.InventorySnapshotCreateDto;
import leoric.pizzacipollastorage.models.InventorySnapshot;
import leoric.pizzacipollastorage.services.interfaces.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping("/snapshot")
    public ResponseEntity<InventorySnapshot> createSnapshot(@RequestBody InventorySnapshotCreateDto dto) {
        return ResponseEntity.ok(inventoryService.createSnapshot(dto));
    }
}