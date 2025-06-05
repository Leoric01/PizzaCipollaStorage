package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.Inventory.InventorySnapshotCreateDto;
import leoric.pizzacipollastorage.DTOs.Inventory.InventorySnapshotResponseDto;
import leoric.pizzacipollastorage.services.interfaces.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping("/snapshot")
    public ResponseEntity<InventorySnapshotResponseDto> createSnapshot(@RequestBody InventorySnapshotCreateDto dto) {
        return ResponseEntity.ok(inventoryService.createSnapshot(dto));
    }

    @GetMapping("/current-status")
    public ResponseEntity<List<InventorySnapshotResponseDto>> getCurrentInventoryStatus() {
        return ResponseEntity.ok(inventoryService.getCurrentInventoryStatus());
    }
}