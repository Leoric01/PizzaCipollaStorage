package leoric.pizzacipollastorage.branch.services.interfaces;

import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.models.Branch;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface BranchServiceAccess {
    Branch verifyAccess(UUID branchId, User user);

    void assertHasAccess(UUID branchId, User user);

    boolean hasAccess(UUID branchId, User user);

    boolean hasRoleOnBranch(UUID branchId, UUID userId, String roleName);

    boolean hasAnyRoleOnBranch(UUID branchId, UUID userId, List<String> roleNames);

    void assertHasRoleOnBranch(UUID branchId, User user, String roleName);
}