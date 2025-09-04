package leoric.pizzacipollastorage.branch.dtos;

import leoric.pizzacipollastorage.common.Address;
import leoric.pizzacipollastorage.common.ContactInfo;

public record BranchCreateDto(
        String name,
        Address address,
        ContactInfo contactInfo
) {
}