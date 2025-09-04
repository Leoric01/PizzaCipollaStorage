package leoric.pizzacipollastorage.init;

import leoric.pizzacipollastorage.auth.models.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface BranchBootstrapService {
    public void bootstrapBranch(UUID branchId, User currentUser);

}