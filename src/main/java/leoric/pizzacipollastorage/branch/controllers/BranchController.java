package leoric.pizzacipollastorage.branch.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.dtos.BranchAccessRequestCreateDto;
import leoric.pizzacipollastorage.branch.dtos.BranchAccessRequestResponseDto;
import leoric.pizzacipollastorage.branch.dtos.BranchCreateDto;
import leoric.pizzacipollastorage.branch.dtos.BranchResponseDto;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchAccessRequestService;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<BranchAccessRequestResponseDto> branchRequestAccess(
            @RequestBody BranchAccessRequestCreateDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(branchAccessRequestService.createRequest(dto, currentUser));
    }

    @GetMapping
    public ResponseEntity<Page<BranchResponseDto>> branchGetAll(
            @RequestParam(required = false) String search,
            @ParameterObject
            @Parameter(required = false) Pageable pageable
    ) {
        return ResponseEntity.ok(branchService.getAllBranches(search, pageable));
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<BranchResponseDto> branchGetByName(@PathVariable String name) {
        return ResponseEntity.ok(branchService.getBranchByName(name));
    }

    @GetMapping("/access-requests/{branchId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<Page<BranchAccessRequestResponseDto>> branchGetAllAccessRequestsByBranchId(
            @PathVariable UUID branchId,
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) String search,
            @ParameterObject
            @Parameter(required = false) Pageable pageable
    ) {
        return ResponseEntity.ok(
                branchAccessRequestService.getAllAccessRequestsByBranch(branchId, currentUser, search, pageable)
        );
    }

    @GetMapping("/access-requests")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<Page<BranchAccessRequestResponseDto>> branchGetAllAccessRequestsToAllMineBranches(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) String search,
            @ParameterObject
            @Parameter(required = false) Pageable pageable
    ) {
        return ResponseEntity.ok(branchAccessRequestService.getAllAccessRequestsToMyBranches(currentUser, search, pageable));
    }

    @GetMapping("/access-requests/mine")
    public ResponseEntity<Page<BranchAccessRequestResponseDto>> branchGetAllAccessRequestsByUser(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) String search,
            @ParameterObject
            @Parameter(required = false) Pageable pageable
    ) {
        return ResponseEntity.ok(branchAccessRequestService.getAllAccessRequestsMine(currentUser, search, pageable));
    }


    @PostMapping("/access-requests/{id}/approve")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<BranchAccessRequestResponseDto> branchApproveAccessRequest(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(branchAccessRequestService.approveRequest(id, currentUser));
    }

    @PostMapping("/access-requests/{id}/reject")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<BranchAccessRequestResponseDto> branchRejectAccessRequest(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(branchAccessRequestService.rejectRequest(id, currentUser));
    }

    @PostMapping("/access-requests/{id}/cancel")
    public ResponseEntity<BranchAccessRequestResponseDto> branchCancelAccessRequest(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(branchAccessRequestService.cancelAccessRequest(id, currentUser));
    }

    @GetMapping("/mine")
    public ResponseEntity<Page<BranchResponseDto>> branchGetMine(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) String search,
            @ParameterObject
            @Parameter(required = false) Pageable pageable
    ) {
        return ResponseEntity.ok(branchService.getBranchesForUser(currentUser, search, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchResponseDto> branchGetById(@PathVariable UUID id) {
        return ResponseEntity.ok(branchService.getBranchById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<Void> branchDeleteById(@PathVariable UUID id, @AuthenticationPrincipal User currentUser) {
        branchService.deleteBranch(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public ResponseEntity<BranchResponseDto> branchUpdateById(
            @PathVariable UUID id,
            @RequestBody BranchCreateDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(branchService.updateBranch(id, dto, currentUser));
    }
}