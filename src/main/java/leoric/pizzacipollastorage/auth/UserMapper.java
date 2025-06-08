package leoric.pizzacipollastorage.auth;

import leoric.pizzacipollastorage.auth.dtos.RegistrationRequest;
import leoric.pizzacipollastorage.auth.dtos.UserResponse;
import leoric.pizzacipollastorage.auth.models.Role;
import leoric.pizzacipollastorage.auth.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Named("mapRolesToNames")
    static List<String> mapRolesToNames(List<Role> roles) {
        return roles.stream().map(Role::getName).collect(Collectors.toList());
    }

    @Mapping(source = "roles", target = "role", qualifiedByName = "mapRolesToNames")
    @Mapping(target = "fullname", expression = "java(user.getFullname())")
    UserResponse userToUserResponse(User user);

    List<UserResponse> usersToUserResponses(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "accountLocked", constant = "false")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(source = "firstname", target = "firstName")
    @Mapping(source = "lastname", target = "lastName")
    User registrationRequestToUser(RegistrationRequest request);
}