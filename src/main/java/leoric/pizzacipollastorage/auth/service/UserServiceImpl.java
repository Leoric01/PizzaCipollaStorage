package leoric.pizzacipollastorage.auth.service;

import jakarta.transaction.Transactional;
import leoric.pizzacipollastorage.auth.UserMapper;
import leoric.pizzacipollastorage.auth.dtos.UserResponseFullDto;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponseFullDto findByUser(User currentUser) {
        currentUser = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.userToUserResponseFull(currentUser);
    }
}
