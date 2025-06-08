package leoric.pizzacipollastorage.auth.service;

import jakarta.mail.MessagingException;
import leoric.pizzacipollastorage.auth.*;
import leoric.pizzacipollastorage.auth.dtos.AuthenticationRequest;
import leoric.pizzacipollastorage.auth.dtos.AuthenticationResponse;
import leoric.pizzacipollastorage.auth.dtos.RegistrationRequest;
import leoric.pizzacipollastorage.auth.dtos.UserResponse;
import leoric.pizzacipollastorage.auth.models.Role;
import leoric.pizzacipollastorage.auth.models.Token;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.auth.repositories.RoleRepository;
import leoric.pizzacipollastorage.auth.repositories.TokenRepository;
import leoric.pizzacipollastorage.auth.repositories.UserRepository;
import leoric.pizzacipollastorage.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Value("${mailing.frontend.activation-url}")
    String activationUrl;

    // TODO : Turned of email validation for development, also swap USER for STUDENT and add TEAChER
    @Override
    public void register(RegistrationRequest request) {
        Role userRole = roleRepository.findByName(request.getRole().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Role " + request.getRole() + " not initialized"));
        User user = userMapper.registrationRequestToUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(List.of(userRole));

        userRepository.save(user);
//        sendValidationEmail(user);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        Map<String, Object> claims = new HashMap<>();
        User user = (User) auth.getPrincipal();
        claims.put("fullName", user.getFullname());
        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Override
    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                // todo exception has to be defined
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been send to the same email address");
        }
        User user = userRepository.findById(savedToken.getUser().getId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

    @Override
    public List<UserResponse> listAll() {
        List<User> users = userRepository.findAll();
        return userMapper.usersToUserResponses(users);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        String newToken = generateAndSaveActivationToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.getFullname(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }
    private String generateAndSaveActivationToken(User user) {
        int length = 6;
        String generatedToken = generateActivationCode(length);
        Token token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }
    private String generateActivationCode(int length) {
        final String chars = "0123456789";
        StringBuilder activationCode = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            activationCode.append(chars.charAt(random.nextInt(chars.length())));
        }
        return activationCode.toString();
    }
}