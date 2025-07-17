package leoric.pizzacipollastorage.branch.repositories;

import leoric.pizzacipollastorage.branch.models.BranchAccessRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BranchAccessRequestRepository extends JpaRepository<BranchAccessRequest, UUID> {
}