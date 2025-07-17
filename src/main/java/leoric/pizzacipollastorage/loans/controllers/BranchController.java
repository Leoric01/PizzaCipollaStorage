package leoric.pizzacipollastorage.loans.controllers;

import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.loans.dtos.BranchCreateDto;
import leoric.pizzacipollastorage.loans.dtos.BranchResponseDto;
import leoric.pizzacipollastorage.services.interfaces.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<BranchResponseDto> branchCreate(@RequestBody BranchCreateDto dto, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(branchService.createBranch(dto, currentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BranchResponseDto> update(@PathVariable UUID id, @RequestBody BranchCreateDto dto) {
        return ResponseEntity.ok(branchService.updateBranch(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<BranchResponseDto>> getAll() {
        return ResponseEntity.ok(branchService.getAllBranches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(branchService.getBranchById(id));
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<BranchResponseDto> getByName(@PathVariable String name) {
        return ResponseEntity.ok(branchService.getBranchByName(name));
    }
}