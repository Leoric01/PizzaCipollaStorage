package leoric.pizzacipollastorage.auth.repositories;

import leoric.pizzacipollastorage.auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String userEmail);

    boolean existsByEmail(String email);
}