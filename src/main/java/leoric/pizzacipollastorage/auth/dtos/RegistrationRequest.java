package leoric.pizzacipollastorage.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @NotEmpty(message = "First name is mandatory")
        @NotBlank(message = "First name is mandatory")
        String firstname,

        @NotEmpty(message = "Last name is mandatory")
        @NotBlank(message = "Last name is mandatory")
        String lastname,

        @NotEmpty(message = "Email name is mandatory")
        @NotBlank(message = "Email name is mandatory")
        @Email(message = "Email is not in valid format")
        String email,

        @NotEmpty(message = "role is mandatory")
        @NotBlank(message = "role is mandatory")
        String role,

        @NotEmpty(message = "Password is mandatory")
        @NotBlank(message = "Password is mandatory")
        @Size(min = 3, message = "Password should be at least 3 characters long")
        @Size(max = 7, message = "Password is too long, it's just study schedule. Keep security appropriate")
        String password
) {
}