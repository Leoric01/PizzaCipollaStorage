package leoric.pizzacipollastorage.branch.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.auth.repositories.UserRepository;
import leoric.pizzacipollastorage.branch.BranchMapper;
import leoric.pizzacipollastorage.branch.dtos.BranchCreateDto;
import leoric.pizzacipollastorage.branch.dtos.BranchResponseDto;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchService;
import leoric.pizzacipollastorage.handler.exceptions.NotAuthorizedForBranchException;
import leoric.pizzacipollastorage.utils.CustomUtilityString;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    @Override
    @Transactional
    public BranchResponseDto createBranch(BranchCreateDto dto, User currentUser) {
        if (currentUser.getBranches() == null) {
            currentUser.setBranches(new ArrayList<>());
        }

        boolean branchExists = currentUser.getBranches().stream()
                .anyMatch(b -> b.getName() != null && b.getName().equalsIgnoreCase(dto.name()));

        if (branchExists) {
            throw new IllegalArgumentException("Už máte pobočku se jménem '" + dto.name() + "'");
        }

        Branch branch = branchMapper.toEntity(dto);
        if (branch == null) {
            throw new IllegalStateException("Mapper returned null branch");
        }

        if (branch.getUsers() == null) {
            branch.setUsers(new ArrayList<>());
        }

        branch.setCreatedByManager(currentUser);
        branch.getUsers().add(currentUser);
        currentUser.getBranches().add(branch);

        branchRepository.save(branch);
        userRepository.save(currentUser);

        BranchResponseDto dtoResponse = branchMapper.toDto(branch);
        if (dtoResponse == null) {
            throw new IllegalStateException("Mapper returned null DTO");
        }

        return dtoResponse;
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

        branch.setName(dto.name());
        branch.setAddress(dto.address());
        branch.setContactInfo(dto.contactInfo());

        return branchMapper.toDto(branchRepository.save(branch));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BranchResponseDto> getAllBranches(String search, Pageable pageable) {
        Page<Branch> page;

        if (search != null && !search.isBlank()) {
            page = branchRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            page = branchRepository.findAll(pageable);
        }

        return page.map(branchMapper::toDto);
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
    public Page<BranchResponseDto> getBranchesForUser(User user, String search, Pageable pageable) {
        List<Branch> branches = user.getBranches();

        Stream<Branch> stream = branches.stream();

        if (search != null && !search.isBlank()) {
            String lowerSearch = search.toLowerCase();
            stream = stream.filter(branch -> branch.getName() != null &&
                    branch.getName().toLowerCase().contains(lowerSearch));
        }

        List<Branch> filtered = stream.toList();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filtered.size());
        List<Branch> pageContent = start > end ? List.of() : filtered.subList(start, end);

        return new PageImpl<>(branchMapper.toDtoList(pageContent), pageable, filtered.size());
    }
}