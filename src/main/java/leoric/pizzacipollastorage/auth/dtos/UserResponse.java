package leoric.pizzacipollastorage.auth.dtos;

import java.util.List;

public record UserResponse(
        String id,
        String email,
        String fullname,
        List<String> role
) {
}