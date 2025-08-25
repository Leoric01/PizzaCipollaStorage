package leoric.pizzacipollastorage.branch.dtos;

import java.util.UUID;

public record BranchResponseDto(
        UUID id,
        String name,
        String address,
        String contactInfo
) {
}