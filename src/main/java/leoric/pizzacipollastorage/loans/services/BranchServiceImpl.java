package leoric.pizzacipollastorage.loans.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.loans.dtos.BranchCreateDto;
import leoric.pizzacipollastorage.loans.dtos.BranchResponseDto;
import leoric.pizzacipollastorage.loans.models.Branch;
import leoric.pizzacipollastorage.loans.repositories.BranchRepository;
import leoric.pizzacipollastorage.mapstruct.BranchMapper;
import leoric.pizzacipollastorage.services.interfaces.BranchService;
import leoric.pizzacipollastorage.utils.CustomUtilityString;
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

    @Override
    public BranchResponseDto getBranchById(UUID id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + id));
        return branchMapper.toDto(branch);
    }

    @Override
    public BranchResponseDto getBranchByName(String name) {
        String normalizedSearch = CustomUtilityString.normalize(name);
        return branchRepository.findAll().stream()
                .filter(branch -> CustomUtilityString.normalize(branch.getName()).equals(normalizedSearch))
                .findFirst()
                .map(branchMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with name: " + name));
    }
}