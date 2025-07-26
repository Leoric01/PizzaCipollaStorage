package leoric.pizzacipollastorage.purchase.controllers;

import leoric.pizzacipollastorage.purchase.services.PurchaseInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/purchase-invoices")
@RequiredArgsConstructor
public class PurchaseInvoiceController {

    private final PurchaseInvoiceService purchaseInvoiceService;

//    @PostMapping
//    public ResponseEntity<PurchaseInvoiceResponseDto> createInvoice(
//            @RequestBody @Valid PurchaseInvoiceCreateDto dto) {
//        return ResponseEntity.ok(purchaseInvoiceService.createInvoice(dto));
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<PurchaseInvoiceResponseDto> getPurchaseInvoiceById(@PathVariable UUID id) {
//        return ResponseEntity.ok(purchaseInvoiceService.getById(id));
//    }
//    @GetMapping
//    public ResponseEntity<List<PurchaseInvoiceResponseDto>> getLatestInvoices() {
//        return ResponseEntity.ok(purchaseInvoiceService.getLatestInvoices(10));
//    }
}