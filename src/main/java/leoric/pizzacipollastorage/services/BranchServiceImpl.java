package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.Loans.BranchCreateDto;
import leoric.pizzacipollastorage.DTOs.Loans.BranchResponseDto;
import leoric.pizzacipollastorage.mapstruct.BranchMapper;
import leoric.pizzacipollastorage.models.Branch;
import leoric.pizzacipollastorage.repositories.BranchRepository;
import leoric.pizzacipollastorage.services.interfaces.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    @Override
    public BranchResponseDto createBranch(BranchCreateDto dto) {
        Branch branch = branchMapper.toEntity(dto);
        return branchMapper.toDto(branchRepository.save(branch));
    }

    @Override
    public void deleteBranch(UUID id) {
        branchRepository.deleteById(id);
    }

    @Override
    public BranchResponseDto updateBranch(UUID id, BranchCreateDto dto) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found: id = " + id));
        branch.setName(dto.getName());
        branch.setAddress(dto.getAddress());
        branch.setContactInfo(dto.getContactInfo());
        return branchMapper.toDto(branchRepository.save(branch));
    }

    @Override
    public List<BranchResponseDto> getAllBranches() {
        return branchMapper.toDtoList(branchRepository.findAll());
    }
}