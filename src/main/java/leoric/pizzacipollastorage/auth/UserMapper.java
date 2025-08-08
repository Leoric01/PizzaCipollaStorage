package leoric.pizzacipollastorage.auth;

import leoric.pizzacipollastorage.auth.dtos.RegistrationRequest;
import leoric.pizzacipollastorage.auth.dtos.UserResponse;
import leoric.pizzacipollastorage.auth.dtos.UserResponseFullDto;
import leoric.pizzacipollastorage.auth.models.Role;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.BranchMapper;
import leoric.pizzacipollastorage.branch.UserBranchRoleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {BranchMapper.class, UserBranchRoleMapper.class})
public interface UserMapper {

    @Named("mapRolesToNames")
    static List<String> mapRolesToNames(List<Role> roles) {
        return roles.stream().map(Role::getName).collect(Collectors.toList());
    }

    @Mapping(source = "roles", target = "role", qualifiedByName = "mapRolesToNames")
    @Mapping(target = "fullname", expression = "java(user.getFullname())")
    UserResponse userToUserResponse(User user);

    @Mapping(source = "roles", target = "role", qualifiedByName = "mapRolesToNames")
    @Mapping(target = "fullname", expression = "java(user.getFullname())")
    @Mapping(source = "branches", target = "branches")
    @Mapping(source = "userBranchRoles", target = "userBranchRoles")
    UserResponseFullDto userToUserResponseFull(User user);

    List<UserResponse> usersToUserResponses(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "accountLocked", constant = "false")
    @Mapping(target = "enabled", constant = "false")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(source = "firstname", target = "firstName")
    @Mapping(source = "lastname", target = "lastName")
    User registrationRequestToUser(RegistrationRequest request);
}
