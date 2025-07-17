package leoric.pizzacipollastorage.branch.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class BranchAccessRequestCreateDto {
    private UUID branchId;
    private UUID userId;
}