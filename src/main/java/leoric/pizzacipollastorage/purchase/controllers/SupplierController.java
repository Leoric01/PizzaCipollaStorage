package leoric.pizzacipollastorage.purchase.controllers;

import leoric.pizzacipollastorage.purchase.dtos.Supplier.SupplierCreateDto;
import leoric.pizzacipollastorage.purchase.dtos.Supplier.SupplierResponseDto;
import leoric.pizzacipollastorage.purchase.services.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    @PostMapping
    public ResponseEntity<SupplierResponseDto> createSupplier(@RequestBody SupplierCreateDto dto) {
        return ResponseEntity.ok(supplierService.createSupplier(dto));
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponseDto>> getAllSuppliers() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }
}