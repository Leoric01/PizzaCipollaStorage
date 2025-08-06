package leoric.pizzacipollastorage.branch.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.auth.repositories.UserRepository;
import leoric.pizzacipollastorage.branch.dtos.BranchCreateDto;
import leoric.pizzacipollastorage.branch.dtos.BranchResponseDto;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchService;
import leoric.pizzacipollastorage.handler.exceptions.NotAuthorizedForBranchException;
import leoric.pizzacipollastorage.mapstruct.BranchMapper;
import leoric.pizzacipollastorage.utils.CustomUtilityString;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    @Override
    @Transactional
    public BranchResponseDto createBranch(BranchCreateDto dto, User currentUser) {
        Branch branch = branchMapper.toEntity(dto);
        if (branch.getUsers() == null) {
            branch.setUsers(new ArrayList<>());
        }
        branch.setCreatedByManager(currentUser);
        branch.getUsers().add(currentUser);
        currentUser.getBranches().add(branch);

        branchRepository.save(branch);
        userRepository.save(currentUser);
        return branchMapper.toDto(branch);
    }

    @Override
    public void deleteBranch(UUID id, User currentUser) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + id));

        if (!currentUser.hasRole("ADMIN") && !branch.getUsers().contains(currentUser)) {
            throw new NotAuthorizedForBranchException("You are not allowed to delete this branch");
        }

        branchRepository.delete(branch);
    }

    @Override
    public BranchResponseDto updateBranch(UUID id, BranchCreateDto dto, User currentUser) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found: id = " + id));

        if (!currentUser.hasRole("ADMIN") && !branch.getUsers().contains(currentUser)) {
            throw new NotAuthorizedForBranchException("You are not allowed to edit this branch");
        }

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

    @Override
    @Transactional(readOnly = true)
    public List<BranchResponseDto> getBranchesForUser(User user) {
        return branchMapper.toDtoList(user.getBranches());
    }
}