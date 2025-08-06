package leoric.pizzacipollastorage.branch.services.interfaces;

import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.dtos.BranchAccessRequestCreateDto;
import leoric.pizzacipollastorage.branch.dtos.BranchAccessRequestResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface BranchAccessRequestService {
    BranchAccessRequestResponseDto createRequest(BranchAccessRequestCreateDto dto, User currentUser);

    List<BranchAccessRequestResponseDto> getAllAccessRequestsByBranch(UUID branchId, User currentUser);

    BranchAccessRequestResponseDto approveRequest(UUID requestId, User currentUser);

    BranchAccessRequestResponseDto rejectRequest(UUID requestId, User currentUser);

    List<BranchAccessRequestResponseDto> getAllAccessRequestsToMyBranches(User currentUser);
}