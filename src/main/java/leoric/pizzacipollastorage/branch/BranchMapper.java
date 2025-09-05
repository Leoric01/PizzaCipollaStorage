package leoric.pizzacipollastorage.branch;

import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.auth.models.UserBranchRole;
import leoric.pizzacipollastorage.branch.dtos.BranchCreateDto;
import leoric.pizzacipollastorage.branch.dtos.BranchCreateWithDataDto;
import leoric.pizzacipollastorage.branch.dtos.BranchResponseDto;
import leoric.pizzacipollastorage.branch.dtos.BranchResponseWithUserRolesDto;
import leoric.pizzacipollastorage.branch.models.Branch;
import org.mapstruct.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    Branch toEntity(BranchCreateDto dto);

    Branch toEntityWithData(BranchCreateWithDataDto dto);

    BranchResponseDto toDto(Branch branch);

    List<BranchResponseDto> toDtoList(List<Branch> branches);

    @Mapping(target = "id", source = "branch.id")
    @Mapping(target = "name", source = "branch.name")
    @Mapping(target = "address", source = "branch.address")
    @Mapping(target = "contactInfo", source = "branch.contactInfo")
    @Mapping(
            target = "userBranchRoles",
            expression = "java(branch.getUserBranchRoles().stream()"
                         + ".filter(ubr -> ubr.getUser().getId().equals(user.getId()))"
                         + ".map(ubr -> ubr.getRole().getName())"
                         + ".collect(java.util.stream.Collectors.toList()))"
    )
    BranchResponseWithUserRolesDto toDtoWithUserRoles(Branch branch, User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Branch entity, BranchCreateDto dto);

    @Named("mapUserBranchRolesToRoleNames")
    static List<String> mapUserBranchRolesToRoleNames(List<UserBranchRole> userBranchRoles) {
        if (userBranchRoles == null) {
            return List.of();
        }
        return userBranchRoles.stream()
                .map(ubr -> ubr.getRole().getName())
                .collect(Collectors.toList());
    }

    default UUID map(Branch branch) {
        return branch.getId();
    }
}