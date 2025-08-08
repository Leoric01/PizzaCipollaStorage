package leoric.pizzacipollastorage.branch;

import leoric.pizzacipollastorage.auth.models.UserBranchRole;
import leoric.pizzacipollastorage.branch.dtos.UserBranchRoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserBranchRoleMapper {

    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "branch.name", target = "branchName")
    @Mapping(source = "role.name", target = "roleName")
    UserBranchRoleDto toDto(UserBranchRole userBranchRole);

    List<UserBranchRoleDto> toDtoList(List<UserBranchRole> roles);
}
