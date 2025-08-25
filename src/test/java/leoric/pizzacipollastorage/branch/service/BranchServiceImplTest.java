package leoric.pizzacipollastorage.branch.service;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.auth.repositories.UserRepository;
import leoric.pizzacipollastorage.branch.BranchMapper;
import leoric.pizzacipollastorage.branch.dtos.BranchCreateDto;
import leoric.pizzacipollastorage.branch.dtos.BranchResponseDto;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.branch.services.BranchServiceImpl;
import leoric.pizzacipollastorage.handler.exceptions.NotAuthorizedForBranchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BranchServiceImplTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private BranchMapper branchMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BranchServiceImpl branchService;

    private User currentUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        currentUser = User.builder()
                .id(UUID.randomUUID())
                .branches(new ArrayList<>())
                .roles(new ArrayList<>())
                .build();
    }

    @Test
    void createBranch_shouldThrow_whenNameExists() {
        currentUser.getBranches().add(
                Branch.builder().name("TestBranch").build()
        );

        BranchCreateDto dto = new BranchCreateDto(
                "TestBranch",
                "123 Main Street",
                "contact@example.com"
        );

        Branch mappedBranch = Branch.builder()
                .name(dto.name())
                .users(new ArrayList<>())
                .build();
        when(branchMapper.toEntity(dto)).thenReturn(mappedBranch);
        when(branchMapper.toDto(mappedBranch)).thenReturn(
                new BranchResponseDto(UUID.randomUUID(), dto.name(), dto.address(), dto.contactInfo())
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                branchService.createBranch(dto, currentUser)
        );

        assertTrue(ex.getMessage().contains("Už máte pobočku se jménem"));
    }

    @Test
    void createBranch_shouldCreateAndSaveBranch_whenValid() {
        BranchCreateDto dto = new BranchCreateDto(
                "NewBranch",
                "123 Main Street",
                "contact@example.com"
        );
        Branch branch = Branch.builder().name("NewBranch").users(new ArrayList<>()).build();
        BranchResponseDto responseDto = new BranchResponseDto(
                UUID.randomUUID(),
                "NewBranch",
                dto.address(),
                dto.contactInfo()
        );

        when(branchMapper.toEntity(dto)).thenReturn(branch);
        when(branchMapper.toDto(branch)).thenReturn(responseDto);
        when(branchRepository.save(branch)).thenReturn(branch);
        when(userRepository.save(currentUser)).thenReturn(currentUser);

        BranchResponseDto result = branchService.createBranch(dto, currentUser);

        assertEquals("NewBranch", result.name());
        assertTrue(branch.getUsers().contains(currentUser));
        assertTrue(currentUser.getBranches().contains(branch));

        verify(branchRepository).save(branch);
        verify(userRepository).save(currentUser);
    }

    @Test
    void deleteBranch_shouldThrow_whenBranchNotFound() {
        UUID branchId = UUID.randomUUID();
        when(branchRepository.findById(branchId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                branchService.deleteBranch(branchId, currentUser)
        );
    }

    @Test
    void deleteBranch_shouldThrow_whenUserNotAuthorized() {
        Branch branch = Branch.builder().users(new ArrayList<>()).build();
        UUID branchId = UUID.randomUUID();
        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));

        assertThrows(NotAuthorizedForBranchException.class, () ->
                branchService.deleteBranch(branchId, currentUser)
        );
    }

    @Test
    void deleteBranch_shouldDelete_whenUserAuthorized() {
        Branch branch = Branch.builder().users(new ArrayList<>()).build();
        branch.getUsers().add(currentUser);
        UUID branchId = UUID.randomUUID();
        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));

        branchService.deleteBranch(branchId, currentUser);

        verify(branchRepository).delete(branch);
    }
}