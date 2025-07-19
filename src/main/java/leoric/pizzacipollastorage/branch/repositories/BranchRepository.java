package leoric.pizzacipollastorage.branch.repositories;

import leoric.pizzacipollastorage.branch.models.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BranchRepository extends JpaRepository<Branch, UUID> {
    Optional<Branch> findByNameIgnoreCase(String name);
}