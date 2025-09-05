package leoric.pizzacipollastorage.branch.repositories;

import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.models.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BranchRepository extends JpaRepository<Branch, UUID> {
    Optional<Branch> findByNameIgnoreCase(String name);

    List<Branch> findAllByCreatedByManager(User manager);

    boolean existsByIdAndUsersContaining(UUID id, User user);

    Page<Branch> findByNameContainingIgnoreCase(String search, Pageable pageable);

    Page<Branch> findByUsersContainingAndNameContainingIgnoreCase(User user, String search, Pageable pageable);

    Page<Branch> findByUsersContaining(User user, Pageable pageable);
}