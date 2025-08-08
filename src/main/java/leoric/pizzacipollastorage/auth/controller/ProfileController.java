package leoric.pizzacipollastorage.auth.controller;

import leoric.pizzacipollastorage.auth.dtos.UserResponseFullDto;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")

public class ProfileController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseFullDto> userMe(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(userService.findByUser(currentUser));
    }
}