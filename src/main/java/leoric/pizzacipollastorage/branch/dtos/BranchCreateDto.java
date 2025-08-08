package leoric.pizzacipollastorage.branch.dtos;

public record BranchCreateDto(
        String name,
        String address,
        String contactInfo
) {
}