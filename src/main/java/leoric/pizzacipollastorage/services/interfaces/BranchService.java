package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.Loans.BranchCreateDto;
import leoric.pizzacipollastorage.DTOs.Loans.BranchResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BranchService {
    BranchResponseDto createBranch(BranchCreateDto dto);
    void deleteBranch(Long id);
    BranchResponseDto updateBranch(Long id, BranchCreateDto dto);
    List<BranchResponseDto> getAllBranches();
}