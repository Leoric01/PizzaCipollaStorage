package leoric.pizzacipollastorage.branch.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchServiceAccess;
import leoric.pizzacipollastorage.handler.exceptions.NotAuthorizedForBranchException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchServiceAccessImpl implements BranchServiceAccess {

    private final BranchRepository branchRepository;

    @Override
    public Branch verifyAccess(UUID branchId, User user) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Pobočka nebyla nalezena."));

        if (!branch.getUsers().contains(user)) {
            throw new NotAuthorizedForBranchException("Uživatel nemá přístup k této pobočce.");
        }
        return branch;
    }

    @Override
    public void assertHasAccess(UUID branchId, User user) {
        if (!branchRepository.existsByIdAndUsersContaining(branchId, user)) {
            throw new NotAuthorizedForBranchException("Uživatel nemá přístup k této pobočce.");
        }
    }

    @Override
    public boolean hasAccess(UUID branchId, User user) {
        return branchRepository.existsByIdAndUsersContaining(branchId, user);
    }
}