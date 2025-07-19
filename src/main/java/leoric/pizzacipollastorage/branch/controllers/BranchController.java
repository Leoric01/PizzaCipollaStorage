package leoric.pizzacipollastorage.branch.controllers;

import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.dtos.BranchAccessRequestCreateDto;
import leoric.pizzacipollastorage.branch.dtos.BranchAccessRequestResponseDto;
import leoric.pizzacipollastorage.branch.dtos.BranchCreateDto;
import leoric.pizzacipollastorage.branch.dtos.BranchResponseDto;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchAccessRequestService;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchService;
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
    private final BranchAccessRequestService branchAccessRequestService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<BranchResponseDto> branchCreate(@RequestBody BranchCreateDto dto, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(branchService.createBranch(dto, currentUser));
    }

    @PostMapping("/access-request")
    public ResponseEntity<BranchAccessRequestResponseDto> requestAccess(
            @RequestBody BranchAccessRequestCreateDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(branchAccessRequestService.createRequest(dto, currentUser));
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

    @GetMapping("/access-requests/{branchId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<List<BranchAccessRequestResponseDto>> getAllAccessRequestsById(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(branchAccessRequestService.getAllByBranch(branchId, currentUser));
    }

    @PostMapping("/access-requests/{id}/approve")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<BranchAccessRequestResponseDto> approveRequest(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(branchAccessRequestService.approveRequest(id, currentUser));
    }

    @PostMapping("/access-requests/{id}/reject")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<BranchAccessRequestResponseDto> rejectRequest(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(branchAccessRequestService.rejectRequest(id, currentUser));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<BranchResponseDto>> getMyBranches(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(branchService.getBranchesForUser(currentUser));
    }
}