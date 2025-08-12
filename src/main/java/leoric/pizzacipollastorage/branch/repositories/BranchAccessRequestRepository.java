package leoric.pizzacipollastorage.branch.repositories;

import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.models.BranchAccessRequest;
import leoric.pizzacipollastorage.branch.models.constants.BranchAccessRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BranchAccessRequestRepository extends JpaRepository<BranchAccessRequest, UUID> {

    boolean existsByUserAndBranchAndBranchAccessRequestStatus(User user, Branch branch, BranchAccessRequestStatus status);

    List<BranchAccessRequest> findAllByBranchOrderByRequestDateDesc(Branch branch);

    List<BranchAccessRequest> findAllByBranchInOrderByRequestDateDesc(List<Branch> branches);

    List<BranchAccessRequest> findAllByUserOrderByRequestDateDesc(User user);

    Page<BranchAccessRequest> findByBranchInAndUserFullnameContainingIgnoreCaseOrderByRequestDateDesc(List<Branch> myBranches, String search, Pageable pageable);

    Page<BranchAccessRequest> findByBranchInOrderByRequestDateDesc(List<Branch> myBranches, Pageable pageable);

    Page<BranchAccessRequest> findByBranchAndUserFullnameContainingIgnoreCaseOrderByRequestDateDesc(Branch branch, String search, Pageable pageable);

    Page<BranchAccessRequest> findByBranchOrderByRequestDateDesc(Branch branch, Pageable pageable);
}