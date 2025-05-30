package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.VatRateCreateDto;
import leoric.pizzacipollastorage.models.VatRate;
import leoric.pizzacipollastorage.services.interfaces.VatRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vat-rates")
@RequiredArgsConstructor
public class VatRateController {
    private final VatRateService vatRateService;

    @PostMapping
    public ResponseEntity<VatRate> createVatRate(@RequestBody VatRateCreateDto dto) {
        return ResponseEntity.ok(vatRateService.createVatRate(dto));
    }

    @GetMapping
    public ResponseEntity<List<VatRate>> getAll() {
        return ResponseEntity.ok(vatRateService.getAll());
    }
}