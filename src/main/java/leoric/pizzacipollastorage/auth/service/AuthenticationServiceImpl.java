package leoric.pizzacipollastorage.auth.service;

import jakarta.mail.MessagingException;
import leoric.pizzacipollastorage.auth.UserMapper;
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
import leoric.pizzacipollastorage.handler.exceptions.EmailAlreadyInUseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

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

    @Override
    public void register(RegistrationRequest request) throws MessagingException {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyInUseException("Email " + request.email() + " is already in use");
        }
        Role userRole = roleRepository.findByName(request.role().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Role " + request.role() + " not initialized"));
        User user = userMapper.registrationRequestToUser(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(List.of(userRole));

        userRepository.save(user);
        sendValidationEmail(user);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        Map<String, Object> claims = new HashMap<>();
        User user = (User) auth.getPrincipal();
        claims.put("id", user.getId());
        claims.put("fullName", user.getFullname());
        String jwtToken = jwtService.generateToken(claims, user);
        Date expiresDate = jwtService.extractExpirationTime(jwtToken);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return new AuthenticationResponse(jwtToken, dateFormat.format(expiresDate));
    }

    @Override
    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                // todo exception has to be defined
                .orElseThrow(() -> new RuntimeException("Invalid token (OTP)"));
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

        String encodedEmail = URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8);
        String fullActivationUrl = activationUrl + newToken + "&email=" + encodedEmail;

        emailService.sendEmail(
                user.getEmail(),
                user.getFullname(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                fullActivationUrl,
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
        // TODO predelat chars na prod
//        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final String chars = "0123456789";
        StringBuilder activationCode = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            activationCode.append(chars.charAt(random.nextInt(chars.length())));
        }
        return activationCode.toString();
    }
}