package leoric.pizzacipollastorage.auth.service;

import jakarta.mail.MessagingException;
import leoric.pizzacipollastorage.auth.dtos.AuthenticationRequest;
import leoric.pizzacipollastorage.auth.dtos.AuthenticationResponse;
import leoric.pizzacipollastorage.auth.dtos.RegistrationRequest;
import leoric.pizzacipollastorage.auth.dtos.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthenticationService {
    void register(RegistrationRequest request) throws MessagingException;

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void activateAccount(String token, String email) throws MessagingException;

    List<UserResponse> listAll();

    List<UserResponse> enableAll();
}