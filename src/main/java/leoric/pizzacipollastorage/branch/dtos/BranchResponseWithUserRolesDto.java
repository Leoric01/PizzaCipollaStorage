package leoric.pizzacipollastorage.branch.dtos;

import leoric.pizzacipollastorage.common.Address;
import leoric.pizzacipollastorage.common.ContactInfo;

import java.util.List;
import java.util.UUID;

public record BranchResponseWithUserRolesDto(
        UUID id,
        String name,
        Address address,
        ContactInfo contactInfo,
        List<String> userBranchRoles
) {
}