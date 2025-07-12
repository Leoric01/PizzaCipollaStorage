package leoric.pizzacipollastorage.auth.dtos;

public record AuthenticationResponse(
        String token,
        String expiresAt
) {
}