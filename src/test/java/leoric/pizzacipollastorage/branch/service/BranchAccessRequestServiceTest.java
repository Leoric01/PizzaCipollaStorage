package leoric.pizzacipollastorage.branch.service;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.auth.repositories.RoleRepository;
import leoric.pizzacipollastorage.auth.repositories.UserBranchRoleRepository;
import leoric.pizzacipollastorage.auth.repositories.UserRepository;
import leoric.pizzacipollastorage.branch.BranchAccessRequestMapper;
import leoric.pizzacipollastorage.branch.dtos.BranchAccessRequestCreateDto;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.models.constants.BranchAccessRequestStatus;
import leoric.pizzacipollastorage.branch.repositories.BranchAccessRequestRepository;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.branch.services.BranchAccessRequestServiceImpl;
import leoric.pizzacipollastorage.branch.services.interfaces.BranchServiceAccess;
import leoric.pizzacipollastorage.handler.BusinessErrorCodes;
import leoric.pizzacipollastorage.handler.exceptions.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BranchAccessRequestServiceTest {
    @Mock
    private BranchRepository branchRepository;

    @Mock
    private BranchAccessRequestRepository accessRequestRepository;

    @Mock
    private UserBranchRoleRepository userBranchRoleRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BranchAccessRequestMapper branchAccessRequestMapper;
    @Mock
    private BranchServiceAccess branchServiceAccess;

    @InjectMocks
    private BranchAccessRequestServiceImpl service;

    private User currentUser;
    private Branch branch;
    private UUID branchId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        branchId = UUID.randomUUID();
        currentUser = User.builder().id(UUID.randomUUID()).branches(new ArrayList<>()).build();
        branch = Branch.builder().id(branchId).users(new ArrayList<>()).build();
    }

    @Test
    void createRequest_shouldThrow_whenBranchNotFound() {
        when(branchRepository.findById(branchId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                service.createRequest(new BranchAccessRequestCreateDto(branchId), currentUser)
        );

        assertTrue(ex.getMessage().contains("Branch not found"));
    }

    @Test
    void createRequest_shouldThrow_whenBranchAlreadyAccessible() {
        branch.getUsers().add(currentUser);
        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                service.createRequest(new BranchAccessRequestCreateDto(branchId), currentUser)
        );

        assertEquals(BusinessErrorCodes.BRANCH_ALREADY_ACCESSIBLE, ex.getErrorCode());
    }

    @Test
    void createRequest_shouldThrow_whenRequestAlreadyPending() {
        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
        when(accessRequestRepository.existsByUserAndBranchAndBranchAccessRequestStatus(
                currentUser, branch, BranchAccessRequestStatus.PENDING
        )).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () ->
                service.createRequest(new BranchAccessRequestCreateDto(branchId), currentUser)
        );

        assertEquals(BusinessErrorCodes.BRANCH_ACCESS_REQUEST_ALREADY_PENDING, ex.getErrorCode());
    }

//    @Test
//    void createRequest_shouldSaveAndReturnDto_whenValid() {
//        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
//        when(accessRequestRepository.existsByUserAndBranchAndBranchAccessRequestStatus(
//                currentUser, branch, BranchAccessRequestStatus.PENDING
//        )).thenReturn(false);
//
//        BranchAccessRequest request = BranchAccessRequest.builder()
//                .branch(branch)
//                .user(currentUser)
//                .branchAccessRequestStatus(BranchAccessRequestStatus.PENDING)
//                .build();
//        BranchAccessRequestResponseDto dto = new BranchAccessRequestResponseDto(
//                UUID.randomUUID(), branch.getId(), "BranchName",
//                currentUser.getId(), "Full Name", "PENDING", LocalDateTime.now(), null, null
//        );
//
//        when(accessRequestRepository.save(any(BranchAccessRequest.class))).thenReturn(request);
//        when(branchAccessRequestMapper.toDto(request)).thenReturn(dto);
//
//        BranchAccessRequestResponseDto result =
//                service.createRequest(new BranchAccessRequestCreateDto(branchId), currentUser);
//
//        assertEquals(dto, result);
//        verify(accessRequestRepository).save(any(BranchAccessRequest.class));
//    }
}