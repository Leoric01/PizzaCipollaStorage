package leoric.pizzacipollastorage.branch.services.interfaces;

import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.dtos.BranchCreateDto;
import leoric.pizzacipollastorage.branch.dtos.BranchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface BranchService {
    BranchResponseDto createBranch(BranchCreateDto dto, User currentUser);

    void deleteBranch(UUID id, User currentUser);

    BranchResponseDto updateBranch(UUID id, BranchCreateDto dto, User currentUser);

    Page<BranchResponseDto> getAllBranches(String search, Pageable pageable);

    BranchResponseDto getBranchById(UUID id);

    BranchResponseDto getBranchByName(String name);

    Page<BranchResponseDto> getBranchesForUser(User user, String search, Pageable pageable);
}