package leoric.pizzacipollastorage.branch.repositories;

import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.models.BranchAccessRequest;
import leoric.pizzacipollastorage.branch.models.constants.BranchAccessRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BranchAccessRequestRepository extends JpaRepository<BranchAccessRequest, UUID> {

    boolean existsByUserAndBranchAndBranchAccessRequestStatus(User user, Branch branch, BranchAccessRequestStatus status);

    List<BranchAccessRequest> findAllByBranchOrderByRequestDateDesc(Branch branch);

    List<BranchAccessRequest> findAllByBranchInOrderByRequestDateDesc(List<Branch> branches);

}