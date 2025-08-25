package leoric.pizzacipollastorage.branch.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record BranchAccessRequestResponseDto(
        UUID id,
        UUID branchId,
        String branchName,
        UUID userId,
        String fullname,
        String status,
        LocalDateTime requestDate,
        LocalDateTime approvalDate,
        String approvedBy
) {
}