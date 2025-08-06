package leoric.pizzacipollastorage.branch.services.interfaces;

import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.models.Branch;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface BranchServiceAccess {
    Branch verifyAccess(UUID branchId, User user);
}