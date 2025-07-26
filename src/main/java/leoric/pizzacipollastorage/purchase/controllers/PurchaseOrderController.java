package leoric.pizzacipollastorage.purchase.controllers;

import leoric.pizzacipollastorage.purchase.services.PurchaseOrderService;
import leoric.pizzacipollastorage.services.interfaces.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;
    private final IngredientService ingredientService;

//    @PostMapping("/generate-auto")
//    public ResponseEntity<PurchaseOrderResponseDto> generateAutoOrder() {
//        PurchaseOrder autoOrder = ingredientService.generateAutoPurchaseOrder();
//        PurchaseOrder saved = purchaseOrderService.save(autoOrder);
//        return ResponseEntity.ok(purchaseOrderService.toDto(saved));
//    }
//
//    @GetMapping
//    public List<PurchaseOrderResponseDto> getAllOrders() {
//        return purchaseOrderService.getAll();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<PurchaseOrderResponseDto> getById(@PathVariable UUID id) {
//        return purchaseOrderService.getById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PostMapping
//    public ResponseEntity<PurchaseOrderResponseDto> create(@RequestBody PurchaseOrderCreateDto dto) {
//        PurchaseOrder created = purchaseOrderService.create(dto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseOrderService.toDto(created));
//    }
//
//    @PutMapping("/{id}/status")
//    public ResponseEntity<Void> updateStatus(
//            @PathVariable UUID id,
//            @RequestParam OrderStatus status
//    ) {
//        purchaseOrderService.updateStatus(id, status);
//        return ResponseEntity.ok().build();
//    }
}