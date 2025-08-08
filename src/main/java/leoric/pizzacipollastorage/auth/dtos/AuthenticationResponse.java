package leoric.pizzacipollastorage.auth.dtos;

import java.util.Date;

public record AuthenticationResponse(
        String token,
        Date expiresAt
) {
}