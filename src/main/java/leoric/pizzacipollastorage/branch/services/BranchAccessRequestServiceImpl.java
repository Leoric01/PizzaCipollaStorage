package leoric.pizzacipollastorage.branch.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.auth.models.Role;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.auth.models.UserBranchRole;
import leoric.pizzacipollastorage.auth.repositories.RoleRepository;
import leoric.pizzacipollastorage.auth.repositories.UserBranchRoleRepository;
import leoric.pizzacipollastorage.auth.repositories.UserRepository;
import leoric.pizzacipollastorage.branch.BranchAccessRequestMapper;
import leoric.pizzacipollastorage.branch.dtos.BranchAccessRequestCreateDto;
import leoric.pizzacipollastorage.branch.dtos.BranchAccessRequestResponseDto;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.models.BranchAccessRequest;
import leoric.pizzacipollastorage.branch.models.constants.BranchAccessRequestStatus;
import leoric.pizzacipollastorage.branch.repositories.BranchAccessRequestRepository;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchAccessRequestService;
import leoric.pizzacipollastorage.handler.BusinessErrorCodes;
import leoric.pizzacipollastorage.handler.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static leoric.pizzacipollastorage.PizzaCipollaStorageApplication.BRANCH_EMPLOYEE;

@Service
@RequiredArgsConstructor
public class BranchAccessRequestServiceImpl implements BranchAccessRequestService {

    private final RoleRepository roleRepository;
    private final UserBranchRoleRepository userBranchRoleRepository;
    private final UserRepository userRepository;
    private final BranchAccessRequestRepository accessRequestRepository;
    private final BranchRepository branchRepository;

    private final BranchAccessRequestMapper branchAccessRequestMapper;

    @Override
    @Transactional
    public BranchAccessRequestResponseDto createRequest(BranchAccessRequestCreateDto dto, User currentUser) {
        Branch branch = branchRepository.findById(dto.branchId())
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));
        System.out.println("Branch: " + branch.getName());
        List<Branch> branches = currentUser.getBranches();
        for (Branch b : branches) {
            System.out.println(b.toString());
        }
        if (branch.getUsers().contains(currentUser)) {
            throw new BusinessException(BusinessErrorCodes.BRANCH_ALREADY_ACCESSIBLE);
        }

        boolean alreadyRequested = accessRequestRepository.existsByUserAndBranchAndBranchAccessRequestStatus(
                currentUser, branch, BranchAccessRequestStatus.PENDING);

        if (alreadyRequested) {
            throw new BusinessException(BusinessErrorCodes.BRANCH_ACCESS_REQUEST_ALREADY_PENDING);
        }

        BranchAccessRequest request = BranchAccessRequest.builder()
                .branch(branch)
                .user(currentUser)
                .branchAccessRequestStatus(BranchAccessRequestStatus.PENDING)
                .requestDate(LocalDateTime.now())
                .build();

        return branchAccessRequestMapper.toDto(accessRequestRepository.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BranchAccessRequestResponseDto> getAllAccessRequestsByBranch(
            UUID branchId,
            User currentUser,
            String search,
            Pageable pageable
    ) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        // kontrola, že manager je skutečně majitel pobočky
        if (!branch.getCreatedByManager().getId().equals(currentUser.getId())) {
            throw new BusinessException(BusinessErrorCodes.NOT_AUTHORIZED_FOR_BRANCH);
        }

        Page<BranchAccessRequest> page;
        if (search != null && !search.isBlank()) {
            page = accessRequestRepository.findByBranchAndUserFullnameContainingIgnoreCaseOrderByRequestDateDesc(branch, search, pageable);
        } else {
            page = accessRequestRepository.findByBranchOrderByRequestDateDesc(branch, pageable);
        }

        return page.map(branchAccessRequestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BranchAccessRequestResponseDto> getAllAccessRequestsToMyBranches(User currentUser, String search, Pageable pageable) {

        List<Branch> myBranches = branchRepository.findAllByCreatedByManager(currentUser);
        if (myBranches.isEmpty()) {
            return Page.empty(pageable);
        }

        Page<BranchAccessRequest> page;
        if (search != null && !search.isBlank()) {
            page = accessRequestRepository
                    .findByBranchInAndUserFullnameContainingIgnoreCaseOrderByRequestDateDesc(myBranches, search, pageable);
        } else {
            page = accessRequestRepository
                    .findByBranchInOrderByRequestDateDesc(myBranches, pageable);
        }

        return page.map(branchAccessRequestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BranchAccessRequestResponseDto> getAllAccessRequestsMine(User currentUser, String search, Pageable pageable) {
        Page<BranchAccessRequest> page;

        if (search != null && !search.isBlank()) {
            page = accessRequestRepository
                    .findByUserAndBranchNameContainingIgnoreCaseOrderByRequestDateDesc(currentUser, search, pageable);
        } else {
            page = accessRequestRepository
                    .findByUserOrderByRequestDateDesc(currentUser, pageable);
        }

        return page.map(branchAccessRequestMapper::toDto);
    }

    @Override
    @Transactional
    public BranchAccessRequestResponseDto cancelAccessRequest(UUID id, User currentUser) {
        BranchAccessRequest request = accessRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Access request not found"));

        if (!request.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You cannot cancel a request you don't own.");
        }

        if (request.getBranchAccessRequestStatus() != BranchAccessRequestStatus.PENDING) {
            throw new IllegalStateException("Only pending requests can be cancelled.");
        }

        request.setBranchAccessRequestStatus(BranchAccessRequestStatus.CANCELLED);
        BranchAccessRequest saved = accessRequestRepository.save(request);

        return branchAccessRequestMapper.toDto(saved);
    }

    @Override
    @Transactional
    public BranchAccessRequestResponseDto approveRequest(UUID requestId, User currentUser) {
        BranchAccessRequest request = accessRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Access request not found"));

        Branch branch = request.getBranch();

        if (!branch.getCreatedByManager().getId().equals(currentUser.getId())) {
            throw new BusinessException(BusinessErrorCodes.NOT_AUTHORIZED_FOR_BRANCH);
        }

        if (request.getBranchAccessRequestStatus() != BranchAccessRequestStatus.PENDING) {
            throw new BusinessException(BusinessErrorCodes.REQUEST_ALREADY_RESOLVED);
        }

        request.setBranchAccessRequestStatus(BranchAccessRequestStatus.APPROVED);
        request.setApprovalDate(LocalDateTime.now());
        request.setApprovedBy(currentUser);

        User targetUser = request.getUser();
        branch.getUsers().add(targetUser);
        targetUser.getBranches().add(branch);
        userRepository.save(targetUser);
        Role employeeRole = roleRepository.findByName(BRANCH_EMPLOYEE)
                .orElseThrow(() -> new EntityNotFoundException("Role " + BRANCH_EMPLOYEE + " not found"));

        UserBranchRole ubr = UserBranchRole.builder()
                .user(targetUser)
                .branch(branch)
                .role(employeeRole)
                .build();
        userBranchRoleRepository.save(ubr);

        return branchAccessRequestMapper.toDto(request);
    }

    @Override
    @Transactional
    public BranchAccessRequestResponseDto rejectRequest(UUID requestId, User currentUser) {
        BranchAccessRequest request = accessRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Access request not found"));

        Branch branch = request.getBranch();

        if (!branch.getCreatedByManager().getId().equals(currentUser.getId())) {
            throw new BusinessException(BusinessErrorCodes.NOT_AUTHORIZED_FOR_BRANCH);
        }

        if (request.getBranchAccessRequestStatus() != BranchAccessRequestStatus.PENDING) {
            throw new BusinessException(BusinessErrorCodes.REQUEST_ALREADY_RESOLVED);
        }

        request.setBranchAccessRequestStatus(BranchAccessRequestStatus.REJECTED);
        request.setApprovalDate(LocalDateTime.now());
        request.setApprovedBy(currentUser);

        return branchAccessRequestMapper.toDto(request);
    }
}