package leoric.pizzacipollastorage.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import leoric.pizzacipollastorage.auth.dtos.AuthenticationRequest;
import leoric.pizzacipollastorage.auth.dtos.AuthenticationResponse;
import leoric.pizzacipollastorage.auth.dtos.RegistrationRequest;
import leoric.pizzacipollastorage.auth.dtos.UserResponse;
import leoric.pizzacipollastorage.auth.service.AuthenticationService;
import leoric.pizzacipollastorage.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {
        authenticationService.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/activate-account")
    public void confirm(@RequestParam String token, @RequestParam String email) throws MessagingException {
        authenticationService.activateAccount(token, email);
    }

    @GetMapping("/list-all")
    public ResponseEntity<List<UserResponse>> usersListAll() {
        return ResponseEntity.ok(authenticationService.listAll());
    }

    // TODO DELETE THIS IN PROD ↓↓↓
    @PostMapping("/god")
    public ResponseEntity<List<UserResponse>> usersEnableAll() {
        return ResponseEntity.ok(authenticationService.listAll());
    }
}