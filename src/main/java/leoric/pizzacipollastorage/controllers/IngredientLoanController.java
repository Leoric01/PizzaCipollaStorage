package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.Loans.IngredientLoanCreateDto;
import leoric.pizzacipollastorage.DTOs.Loans.IngredientLoanResponseDto;
import leoric.pizzacipollastorage.services.interfaces.IngredientLoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<IngredientLoanResponseDto> returnLoan(@PathVariable Long id) {
        return ResponseEntity.ok(ingredientLoanService.markLoanAsReturned(id));
    }

    @GetMapping
    public ResponseEntity<List<IngredientLoanResponseDto>> getAll() {
        return ResponseEntity.ok(ingredientLoanService.getAllLoans());
    }
}