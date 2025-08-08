package leoric.pizzacipollastorage.branch.services.interfaces;

import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.dtos.BranchCreateDto;
import leoric.pizzacipollastorage.branch.dtos.BranchResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface BranchService {
    BranchResponseDto createBranch(BranchCreateDto dto, User currentUser);

    void deleteBranch(UUID id, User currentUser);

    BranchResponseDto updateBranch(UUID id, BranchCreateDto dto, User currentUser);

    List<BranchResponseDto> getAllBranches();

    BranchResponseDto getBranchById(UUID id);

    BranchResponseDto getBranchByName(String name);

    List<BranchResponseDto> getBranchesForUser(User user);
}