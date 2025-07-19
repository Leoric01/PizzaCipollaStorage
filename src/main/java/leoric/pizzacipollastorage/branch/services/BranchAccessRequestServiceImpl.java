package leoric.pizzacipollastorage.branch.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.auth.repositories.UserRepository;
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
import leoric.pizzacipollastorage.mapstruct.BranchAccessRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchAccessRequestServiceImpl implements BranchAccessRequestService {

    private final UserRepository userRepository;
    private final BranchAccessRequestRepository accessRequestRepository;
    private final BranchRepository branchRepository;

    private final BranchAccessRequestMapper branchAccessRequestMapper;

    @Override
    @Transactional
    public BranchAccessRequestResponseDto createRequest(BranchAccessRequestCreateDto dto, User currentUser) {
        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        if (currentUser.getBranches().contains(branch)) {
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
    public List<BranchAccessRequestResponseDto> getAllByBranch(UUID branchId, User currentUser) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        if (!branch.getCreatedByManager().getId().equals(currentUser.getId())) {
            throw new BusinessException(BusinessErrorCodes.NOT_AUTHORIZED_FOR_BRANCH);
        }

        List<BranchAccessRequest> requests = accessRequestRepository.findAllByBranchOrderByRequestDateDesc(branch);
        return branchAccessRequestMapper.toDtoList(requests);
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