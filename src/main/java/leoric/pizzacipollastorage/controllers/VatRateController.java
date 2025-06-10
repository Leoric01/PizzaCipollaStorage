package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.Vat.VatRateCreateDto;
import leoric.pizzacipollastorage.DTOs.Vat.VatRateDeleteResponseDto;
import leoric.pizzacipollastorage.DTOs.Vat.VatRateShortDto;
import leoric.pizzacipollastorage.models.VatRate;
import leoric.pizzacipollastorage.services.interfaces.VatRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vat-rates")
@RequiredArgsConstructor
public class VatRateController {
    private final VatRateService vatRateService;

    @PostMapping
    public ResponseEntity<VatRate> createVatRate(@RequestBody VatRateCreateDto dto) {
        VatRate created = vatRateService.createVatRate(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<VatRateShortDto>> getAll() {
        return ResponseEntity.ok(vatRateService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<VatRateDeleteResponseDto> deleteVatRate(@PathVariable UUID id) {
        VatRateDeleteResponseDto deleted = vatRateService.deleteVatRateById(id);
        return ResponseEntity.ok(deleted);
    }
}