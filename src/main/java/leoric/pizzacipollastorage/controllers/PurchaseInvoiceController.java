package leoric.pizzacipollastorage.controllers;

import jakarta.validation.Valid;
import leoric.pizzacipollastorage.DTOs.PurchaseInvoice.PurchaseInvoiceCreateDto;
import leoric.pizzacipollastorage.DTOs.PurchaseInvoice.PurchaseInvoiceResponseDto;
import leoric.pizzacipollastorage.services.interfaces.PurchaseInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-invoices")
@RequiredArgsConstructor
public class PurchaseInvoiceController {

    private final PurchaseInvoiceService purchaseInvoiceService;

    @PostMapping
    public ResponseEntity<PurchaseInvoiceResponseDto> createInvoice(
            @RequestBody @Valid PurchaseInvoiceCreateDto dto) {
        return ResponseEntity.ok(purchaseInvoiceService.createInvoice(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseInvoiceResponseDto> getPurchaseInvoiceById(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseInvoiceService.getById(id));
    }
    @GetMapping
    public ResponseEntity<List<PurchaseInvoiceResponseDto>> getLatestInvoices() {
        return ResponseEntity.ok(purchaseInvoiceService.getLatestInvoices(10));
    }
}