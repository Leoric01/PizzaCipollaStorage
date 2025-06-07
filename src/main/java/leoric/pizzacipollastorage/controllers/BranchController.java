package leoric.pizzacipollastorage.controllers;

import leoric.pizzacipollastorage.DTOs.Loans.BranchCreateDto;
import leoric.pizzacipollastorage.DTOs.Loans.BranchResponseDto;
import leoric.pizzacipollastorage.services.interfaces.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    public ResponseEntity<BranchResponseDto> create(@RequestBody BranchCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(branchService.createBranch(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BranchResponseDto> update(@PathVariable Long id, @RequestBody BranchCreateDto dto) {
        return ResponseEntity.ok(branchService.updateBranch(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<BranchResponseDto>> getAll() {
        return ResponseEntity.ok(branchService.getAllBranches());
    }
}