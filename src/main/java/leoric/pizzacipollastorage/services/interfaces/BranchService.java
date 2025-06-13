package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.Loans.BranchCreateDto;
import leoric.pizzacipollastorage.DTOs.Loans.BranchResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface BranchService {
    BranchResponseDto createBranch(BranchCreateDto dto);

    void deleteBranch(UUID id);

    BranchResponseDto updateBranch(UUID id, BranchCreateDto dto);
    List<BranchResponseDto> getAllBranches();

    BranchResponseDto getBranchById(UUID id);

    BranchResponseDto getBranchByName(String name);
}