package leoric.pizzacipollastorage.auth.service;

import jakarta.mail.MessagingException;
import leoric.pizzacipollastorage.auth.UserMapper;
import leoric.pizzacipollastorage.auth.dtos.RegistrationRequest;
import leoric.pizzacipollastorage.auth.models.Role;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.auth.repositories.RoleRepository;
import leoric.pizzacipollastorage.auth.repositories.UserRepository;
import leoric.pizzacipollastorage.handler.exceptions.EmailAlreadyInUseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationServiceImpl authenticationServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {
        RegistrationRequest request = new RegistrationRequest(
                "John", "Doe", "test@example.com", "ADMIN", "pass"
        );
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyInUseException.class, () -> {
            authenticationServiceImpl.register(request);
        });

        verify(userRepository).existsByEmail("test@example.com");
        verifyNoMoreInteractions(userRepository, roleRepository, userMapper, passwordEncoder);
    }

    @Test
    void register_shouldThrowException_whenRoleNotFound() {
        RegistrationRequest request = new RegistrationRequest(
                "John", "Doe", "test@example.com", "ADMIN", "pass"
        );
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            authenticationServiceImpl.register(request);
        });

        assertTrue(ex.getMessage().contains("Role ADMIN not initialized"));
        verify(userRepository).existsByEmail(request.email());
        verify(roleRepository).findByName("ADMIN");
    }

    @Test
    void register_shouldSaveUserAndSendEmail_whenValidRequest() throws MessagingException {
        RegistrationRequest request = new RegistrationRequest(
                "John", "Doe", "test@example.com", "ADMIN", "pass"
        );

        Role role = Role.builder().id(1).name("ADMIN").build();
        User mappedUser = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .build();

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(role));
        when(userMapper.registrationRequestToUser(request)).thenReturn(mappedUser);
        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthenticationServiceImpl spyService = Mockito.spy(authenticationServiceImpl);
        doNothing().when(spyService).sendValidationEmail(any(User.class));

        spyService.register(request);

        assertEquals("encodedPass", mappedUser.getPassword());
        assertEquals(1, mappedUser.getRoles().size());
        assertEquals(role, mappedUser.getRoles().get(0));

        verify(userRepository).save(mappedUser);
        verify(spyService).sendValidationEmail(mappedUser);
    }
}