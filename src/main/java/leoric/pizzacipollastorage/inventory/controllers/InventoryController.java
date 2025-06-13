package leoric.pizzacipollastorage.inventory.controllers;

import leoric.pizzacipollastorage.inventory.dtos.Inventory.InventorySnapshotCreateDto;
import leoric.pizzacipollastorage.inventory.dtos.Inventory.InventorySnapshotResponseDto;
import leoric.pizzacipollastorage.inventory.services.InventoryService;
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

    @PostMapping("/snapshot/bulk")
    public ResponseEntity<List<InventorySnapshotResponseDto>> createSnapshotBulk(
            @RequestBody List<InventorySnapshotCreateDto> dtos) {
        return ResponseEntity.ok(inventoryService.createSnapshotBulk(dtos));
    }
}