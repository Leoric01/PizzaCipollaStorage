package leoric.pizzacipollastorage.branch.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BranchAccessRequestResponseDto {
    private UUID id;
    private UUID branchId;
    private String branchName;
    private UUID userId;
    private String fullname;
    private String status;
    private LocalDateTime requestDate;
    private LocalDateTime approvalDate;
    private String approvedBy;
}