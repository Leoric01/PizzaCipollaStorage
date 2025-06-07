package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
}