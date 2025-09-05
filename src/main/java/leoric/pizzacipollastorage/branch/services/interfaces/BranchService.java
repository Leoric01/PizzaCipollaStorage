package leoric.pizzacipollastorage.branch.services.interfaces;

import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.dtos.BranchCreateDto;
import leoric.pizzacipollastorage.branch.dtos.BranchCreateWithDataDto;
import leoric.pizzacipollastorage.branch.dtos.BranchResponseDto;
import leoric.pizzacipollastorage.branch.dtos.BranchResponseWithUserRolesDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface BranchService {
    BranchResponseDto createBranch(BranchCreateDto dto, User currentUser);

    void deleteBranch(UUID id, User currentUser);

    BranchResponseDto updateBranch(UUID id, BranchCreateDto dto, User currentUser);

    Page<BranchResponseWithUserRolesDto> getAllBranches(String search, Pageable pageable, User currentUser);

    BranchResponseDto getBranchById(UUID id);

    BranchResponseDto getBranchByName(String name);

    Page<BranchResponseWithUserRolesDto> getBranchesForUser(User user, String search, Pageable pageable);

    BranchResponseDto createBranchWithData(BranchCreateWithDataDto dto, User currentUser);

    void leaveBranchAccess(UUID branchId, User currentUser);

    void branchAccessKickUserById(UUID branchId, User currentUser, UUID targetUserId);
}