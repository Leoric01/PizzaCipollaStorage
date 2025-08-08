package leoric.pizzacipollastorage.branch.dtos;

import java.util.UUID;

public record UserBranchRoleDto(
        UUID branchId,
        String branchName,
        String roleName
) {
}
