package leoric.pizzacipollastorage.branch.service;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.auth.repositories.UserBranchRoleRepository;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.branch.services.BranchServiceAccessImpl;
import leoric.pizzacipollastorage.handler.exceptions.NotAuthorizedForBranchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BranchServiceAccessImplTest {
    @Mock
    private BranchRepository branchRepository;

    @Mock
    private UserBranchRoleRepository userBranchRoleRepository;

    @InjectMocks
    private BranchServiceAccessImpl branchServiceAccess;

    private User user;
    private UUID branchId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder().id(UUID.randomUUID()).build();
        branchId = UUID.randomUUID();
    }

    @Test
    void assertHasRoleOnBranch_shouldPass_whenUserHasRole() {
        when(userBranchRoleRepository.existsByUserIdAndBranchIdAndRoleName(user.getId(), branchId, "MANAGER"))
                .thenReturn(true);

        assertDoesNotThrow(() ->
                branchServiceAccess.assertHasRoleOnBranch(branchId, user, "MANAGER")
        );
    }

    @Test
    void assertHasRoleOnBranch_shouldThrow_whenUserHasNoRole() {
        when(userBranchRoleRepository.existsByUserIdAndBranchIdAndRoleName(any(), any(), any()))
                .thenReturn(false);

        NotAuthorizedForBranchException ex = assertThrows(NotAuthorizedForBranchException.class, () ->
                branchServiceAccess.assertHasRoleOnBranch(branchId, user, "MANAGER;ADMIN")
        );

        assertTrue(ex.getMessage().contains("Uživatel nemá žádnou z povolených rolí"));
    }

    @Test
    void assertHasRoleOnBranch_shouldPass_whenUserHasOneOfMultipleRoles() {

        when(userBranchRoleRepository.existsByUserIdAndBranchIdAndRoleName(user.getId(), branchId, "MANAGER"))
                .thenReturn(false);
        when(userBranchRoleRepository.existsByUserIdAndBranchIdAndRoleName(user.getId(), branchId, "ADMIN"))
                .thenReturn(true);

        assertDoesNotThrow(() ->
                branchServiceAccess.assertHasRoleOnBranch(branchId, user, "MANAGER;ADMIN")
        );
    }

    @Test
    void verifyAccess_shouldThrow_whenBranchNotFound() {
        when(branchRepository.findById(branchId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                branchServiceAccess.verifyAccess(branchId, user)
        );

        assertTrue(ex.getMessage().contains("Pobočka nebyla nalezena"));
    }

    @Test
    void verifyAccess_shouldThrow_whenUserNotMember() {
        Branch branch = Branch.builder()
                .id(branchId)
                .users(new ArrayList<>())
                .build();
        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));

        NotAuthorizedForBranchException ex = assertThrows(NotAuthorizedForBranchException.class, () ->
                branchServiceAccess.verifyAccess(branchId, user)
        );

        assertTrue(ex.getMessage().contains("Uživatel nemá přístup"));
    }

    @Test
    void verifyAccess_shouldReturnBranch_whenUserIsMember() {
        Branch branch = Branch.builder()
                .id(branchId)
                .users(new ArrayList<>(List.of(user)))
                .build();
        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));

        Branch result = branchServiceAccess.verifyAccess(branchId, user);

        assertEquals(branch, result);
    }
}