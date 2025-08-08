package leoric.pizzacipollastorage.auth.repositories;

import leoric.pizzacipollastorage.auth.models.UserBranchRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserBranchRoleRepository extends JpaRepository<UserBranchRole, Long> {

    boolean existsByUserIdAndBranchIdAndRoleName(UUID userId, UUID branchId, String roleName);

    List<UserBranchRole> findByUserIdAndBranchId(UUID userId, UUID branchId);
}