package leoric.pizzacipollastorage.auth.service;

import leoric.pizzacipollastorage.auth.dtos.UserResponseFullDto;
import leoric.pizzacipollastorage.auth.models.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserResponseFullDto findByUser(User currentUser);
}
