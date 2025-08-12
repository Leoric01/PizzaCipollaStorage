package leoric.pizzacipollastorage.branch.services.interfaces;

import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.dtos.BranchAccessRequestCreateDto;
import leoric.pizzacipollastorage.branch.dtos.BranchAccessRequestResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface BranchAccessRequestService {
    BranchAccessRequestResponseDto createRequest(BranchAccessRequestCreateDto dto, User currentUser);

    Page<BranchAccessRequestResponseDto> getAllAccessRequestsByBranch(
            UUID branchId,
            User currentUser,
            String search,
            Pageable pageable
    );

    BranchAccessRequestResponseDto approveRequest(UUID requestId, User currentUser);

    BranchAccessRequestResponseDto rejectRequest(UUID requestId, User currentUser);

    Page<BranchAccessRequestResponseDto> getAllAccessRequestsToMyBranches(User currentUser, String search, Pageable pageable);

    List<BranchAccessRequestResponseDto> getAllAccessRequestsMine(User currentUser);

    BranchAccessRequestResponseDto cancelAccessRequest(UUID id, User currentUser);
}