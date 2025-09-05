package leoric.pizzacipollastorage.branch.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.auth.models.Role;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.auth.models.UserBranchRole;
import leoric.pizzacipollastorage.auth.repositories.RoleRepository;
import leoric.pizzacipollastorage.auth.repositories.UserBranchRoleRepository;
import leoric.pizzacipollastorage.auth.repositories.UserRepository;
import leoric.pizzacipollastorage.branch.BranchMapper;
import leoric.pizzacipollastorage.branch.dtos.BranchCreateDto;
import leoric.pizzacipollastorage.branch.dtos.BranchCreateWithDataDto;
import leoric.pizzacipollastorage.branch.dtos.BranchResponseDto;
import leoric.pizzacipollastorage.branch.dtos.BranchResponseWithUserRolesDto;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchService;
import leoric.pizzacipollastorage.handler.exceptions.NotAuthorizedForBranchException;
import leoric.pizzacipollastorage.init.BranchBootstrapService;
import leoric.pizzacipollastorage.utils.CustomUtilityString;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static leoric.pizzacipollastorage.PizzaCipollaStorageApplication.ADMIN;
import static leoric.pizzacipollastorage.PizzaCipollaStorageApplication.BRANCH_MANAGER;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserBranchRoleRepository userBranchRoleRepository;
    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;
    private final BranchBootstrapService branchBootstrapService;

    @Override
    public BranchResponseDto createBranchWithData(BranchCreateWithDataDto dto, User currentUser) {
        if (currentUser.getBranches() == null) {
            currentUser.setBranches(new ArrayList<>());
        }

        boolean branchExists = currentUser.getBranches().stream()
                .anyMatch(b -> b.getName() != null && b.getName().equalsIgnoreCase(dto.name()));

        if (branchExists) {
            throw new IllegalArgumentException("Už máte pobočku se jménem '" + dto.name() + "'");
        }

        Branch branch = branchMapper.toEntityWithData(dto);

        if (branch.getUsers() == null) {
            branch.setUsers(new ArrayList<>());
        }

        branch.setCreatedByManager(currentUser);
        branch.getUsers().add(currentUser);
        currentUser.getBranches().add(branch);
        branchRepository.save(branch);

        Role managerRole = roleRepository.findByName(BRANCH_MANAGER)
                .orElseThrow(() -> new EntityNotFoundException("Role " + BRANCH_MANAGER + " not found, contact admin"));

        UserBranchRole ubr = UserBranchRole.builder()
                .user(currentUser)
                .branch(branch)
                .role(managerRole)
                .build();
        userBranchRoleRepository.save(ubr);

        userRepository.save(currentUser);

        BranchResponseDto dtoResponse = branchMapper.toDto(branch);

        if (dto.withDefaultData()) {
            branchBootstrapService.bootstrapBranch(branch.getId(), currentUser);
        }

        return dtoResponse;
    }

    @Override
    @Transactional
    public void leaveBranchAccess(UUID branchId, User user) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found: " + branchId));

        List<UserBranchRole> toRemove = user.getUserBranchRoles().stream()
                .filter(ubr -> ubr.getBranch().getId().equals(branchId))
                .toList();

        if (!toRemove.isEmpty()) {
            user.getUserBranchRoles().removeAll(toRemove);
            branch.getUserBranchRoles().removeAll(toRemove);
        }

        user.getBranches().remove(branch);
        branch.getUsers().remove(user);

        userRepository.save(user);
        branchRepository.save(branch);
    }

    @Override
    @Transactional
    public void branchAccessKickUserById(UUID branchId, User currentUser, UUID targetUserId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found: " + branchId));

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new EntityNotFoundException("Target user not found: " + targetUserId));

        if (currentUser.getId().equals(targetUserId)) {
            throw new IllegalArgumentException("A user cannot kick themselves from a branch.");
        }

        List<UserBranchRole> rolesToRemove = targetUser.getUserBranchRoles().stream()
                .filter(ubr -> ubr.getBranch().getId().equals(branchId))
                .toList();

        if (!rolesToRemove.isEmpty()) {
            targetUser.getUserBranchRoles().removeAll(rolesToRemove);
            branch.getUserBranchRoles().removeAll(rolesToRemove);
            userBranchRoleRepository.deleteAll(rolesToRemove);
        }

        targetUser.getBranches().remove(branch);
        branch.getUsers().remove(targetUser);

        userRepository.save(targetUser);
        branchRepository.save(branch);
    }

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

        Role managerRole = roleRepository.findByName(BRANCH_MANAGER)
                .orElseThrow(() -> new EntityNotFoundException("Role " + BRANCH_MANAGER + " not found, contact admin"));

        UserBranchRole ubr = UserBranchRole.builder()
                .user(currentUser)
                .branch(branch)
                .role(managerRole)
                .build();
        userBranchRoleRepository.save(ubr);

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

        if (!currentUser.hasRole(ADMIN) && !branch.getUsers().contains(currentUser)) {
            throw new NotAuthorizedForBranchException("You are not allowed to delete this branch");
        }

        branchRepository.delete(branch);
    }

    @Override
    public BranchResponseDto updateBranch(UUID id, BranchCreateDto dto, User currentUser) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found: id = " + id));

        if (!currentUser.hasRole(ADMIN) && !branch.getUsers().contains(currentUser)) {
            throw new NotAuthorizedForBranchException("You are not allowed to edit this branch");
        }

        branch.setName(dto.name());
        branch.setAddress(dto.address());
        branch.setContactInfo(dto.contactInfo());

        return branchMapper.toDto(branchRepository.save(branch));
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
    public Page<BranchResponseWithUserRolesDto> getAllBranches(String search, Pageable pageable, User currentUser) {
        Page<Branch> page;

        if (search != null && !search.isBlank()) {
            page = branchRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            page = branchRepository.findAll(pageable);
        }

        return page.map(branch -> branchMapper.toDtoWithUserRoles(branch, currentUser));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BranchResponseWithUserRolesDto> getBranchesForUser(User user, String search, Pageable pageable) {
        Page<Branch> page;

        if (search != null && !search.isBlank()) {
            page = branchRepository.findByUsersContainingAndNameContainingIgnoreCase(user, search, pageable);
        } else {
            page = branchRepository.findByUsersContaining(user, pageable);
        }

        return page.map(branch -> branchMapper.toDtoWithUserRoles(branch, user));
    }
}