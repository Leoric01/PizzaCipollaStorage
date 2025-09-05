package leoric.pizzacipollastorage.branch.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.auth.models.Role;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.auth.models.UserBranchRole;
import leoric.pizzacipollastorage.auth.repositories.UserBranchRoleRepository;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchServiceAccess;
import leoric.pizzacipollastorage.handler.exceptions.NotAuthorizedForBranchException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static leoric.pizzacipollastorage.PizzaCipollaStorageApplication.ADMIN;

@Service
@RequiredArgsConstructor
public class BranchServiceAccessImpl implements BranchServiceAccess {

    private final BranchRepository branchRepository;
    private final UserBranchRoleRepository userBranchRoleRepository;

    @Override
    public void assertHasRoleOnBranch(UUID branchId, User user, String allowedRoles) {
        if (user.hasGlobalRole(ADMIN)) {
            return;
        }

        String[] rolesArray = allowedRoles.split(";");
        for (String role : rolesArray) {
            if (userBranchRoleRepository.existsByUserIdAndBranchIdAndRoleName(user.getId(), branchId, role.trim())) {
                return;
            }
        }
        throw new NotAuthorizedForBranchException(
                "Uživatel nemá žádnou z povolených rolí [" + allowedRoles + "] pro tuto pobočku."
        );
    }

    @Override
    public List<Role> getRolesOnBranch(UUID branchId, UUID userId) {
        return userBranchRoleRepository.findByUserIdAndBranchId(userId, branchId)
                .stream()
                .map(UserBranchRole::getRole)
                .toList();
    }

    @Override
    public List<String> getRoleNamesOnBranch(UUID branchId, UUID userId) {
        return userBranchRoleRepository.findByUserIdAndBranchId(userId, branchId)
                .stream()
                .map(ubr -> ubr.getRole().getName())
                .toList();
    }

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

    @Override
    public boolean hasRoleOnBranch(UUID branchId, UUID userId, String roleName) {
        return userBranchRoleRepository.existsByUserIdAndBranchIdAndRoleName(userId, branchId, roleName);
    }

    @Override
    public boolean hasAnyRoleOnBranch(UUID branchId, UUID userId, List<String> roleNames) {
        return userBranchRoleRepository.findByUserIdAndBranchId(userId, branchId).stream()
                .anyMatch(ubr -> roleNames.contains(ubr.getRole().getName()));
    }

}