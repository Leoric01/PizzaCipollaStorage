package leoric.pizzacipollastorage.purchase.controllers;

import leoric.pizzacipollastorage.purchase.services.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

//    @PostMapping
//    public ResponseEntity<SupplierResponseDto> createSupplier(@RequestBody SupplierCreateDto dto) {
//        return ResponseEntity.ok(supplierService.createSupplier(dto));
//    }
//
//    @GetMapping
//    public ResponseEntity<List<SupplierResponseDto>> getAllSuppliers() {
//        return ResponseEntity.ok(supplierService.getAllSuppliers());
//    }
}