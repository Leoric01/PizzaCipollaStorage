package leoric.pizzacipollastorage.loans.controllers;

import leoric.pizzacipollastorage.loans.dtos.IngredientLoanCreateDto;
import leoric.pizzacipollastorage.loans.dtos.IngredientLoanPatchDto;
import leoric.pizzacipollastorage.loans.dtos.IngredientLoanResponseDto;
import leoric.pizzacipollastorage.services.interfaces.IngredientLoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class IngredientLoanController {

    private final IngredientLoanService ingredientLoanService;

    @PostMapping
    public ResponseEntity<IngredientLoanResponseDto> createLoan(@RequestBody IngredientLoanCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ingredientLoanService.createLoan(dto));
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<IngredientLoanResponseDto> returnLoan(@PathVariable UUID id) {
        return ResponseEntity.ok(ingredientLoanService.markLoanAsReturned(id));
    }

    @GetMapping
    public ResponseEntity<List<IngredientLoanResponseDto>> getAll() {
        return ResponseEntity.ok(ingredientLoanService.getAllLoans());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<IngredientLoanResponseDto> patchLoan(
            @PathVariable UUID id,
            @RequestBody IngredientLoanPatchDto dto) {
        return ResponseEntity.ok(ingredientLoanService.patchLoan(id, dto));
    }
}