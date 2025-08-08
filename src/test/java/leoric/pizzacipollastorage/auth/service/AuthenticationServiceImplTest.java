package leoric.pizzacipollastorage.auth.service;

import leoric.pizzacipollastorage.auth.UserMapper;
import leoric.pizzacipollastorage.auth.dtos.RegistrationRequest;
import leoric.pizzacipollastorage.auth.repositories.RoleRepository;
import leoric.pizzacipollastorage.auth.repositories.UserRepository;
import leoric.pizzacipollastorage.handler.exceptions.EmailAlreadyInUseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private UserServiceImpl userService;
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
}