package leoric.pizzacipollastorage.auth.dtos;

import leoric.pizzacipollastorage.branch.dtos.UserBranchRoleDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserResponseFullDto(
        UUID id,
        String firstName,
        String lastName,
        String fullname,
        String email,
        boolean accountLocked,
        boolean enabled,
        List<String> role,
        List<UUID> branches,
        List<UserBranchRoleDto> userBranchRoles,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
}