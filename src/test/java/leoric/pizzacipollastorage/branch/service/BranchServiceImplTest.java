package leoric.pizzacipollastorage.branch.service;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.auth.models.Role;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.auth.repositories.RoleRepository;
import leoric.pizzacipollastorage.auth.repositories.UserBranchRoleRepository;
import leoric.pizzacipollastorage.auth.repositories.UserRepository;
import leoric.pizzacipollastorage.branch.BranchMapper;
import leoric.pizzacipollastorage.branch.dtos.BranchCreateDto;
import leoric.pizzacipollastorage.branch.dtos.BranchResponseDto;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.branch.services.BranchServiceImpl;
import leoric.pizzacipollastorage.common.Address;
import leoric.pizzacipollastorage.common.ContactInfo;
import leoric.pizzacipollastorage.handler.exceptions.NotAuthorizedForBranchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BranchServiceImplTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private BranchMapper branchMapper;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserBranchRoleRepository userBranchRoleRepository;

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

        Address address = Address.builder()
                .city("PRG")
                .build();

        ContactInfo contactInfo = ContactInfo.builder()
                .contactEmail("contact@example.com")
                .build();
        BranchCreateDto dto = new BranchCreateDto(
                "TestBranch",
                address,
                contactInfo

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
        Address address = Address.builder().city("PRG").build();
        ContactInfo contactInfo = ContactInfo.builder().contactEmail("contact@example.com").build();

        BranchCreateDto dto = new BranchCreateDto("TestBranch", address, contactInfo);
        BranchResponseDto responseDto = new BranchResponseDto(UUID.randomUUID(), "NewBranch", dto.address(), dto.contactInfo());
        Branch branch = Branch.builder().name("NewBranch").users(new ArrayList<>()).build();

        when(branchMapper.toEntity(dto)).thenReturn(branch);
        when(branchMapper.toDto(branch)).thenReturn(responseDto);
        when(branchRepository.save(branch)).thenReturn(branch);
        when(userRepository.save(currentUser)).thenReturn(currentUser);

        Role branchManagerRole = new Role();
        branchManagerRole.setName("BRANCH_MANAGER");
        when(roleRepository.findByName("BRANCH_MANAGER")).thenReturn(Optional.of(branchManagerRole));

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
    void updateBranch_shouldThrow_whenBranchNotFound() {
        UUID branchId = UUID.randomUUID();
        BranchCreateDto dto = new BranchCreateDto("Updated", null, null);
        when(branchRepository.findById(branchId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                branchService.updateBranch(branchId, dto, currentUser)
        );
    }

    @Test
    void updateBranch_shouldThrow_whenUserNotAuthorized() {
        UUID branchId = UUID.randomUUID();
        Branch branch = Branch.builder().users(new ArrayList<>()).build();
        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
        BranchCreateDto dto = new BranchCreateDto("Updated", null, null);

        assertThrows(NotAuthorizedForBranchException.class, () ->
                branchService.updateBranch(branchId, dto, currentUser)
        );
    }

    @Test
    void updateBranch_shouldUpdate_whenUserAuthorized() {
        UUID branchId = UUID.randomUUID();
        Branch branch = Branch.builder().name("Old").users(new ArrayList<>()).build();
        branch.getUsers().add(currentUser);

        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
        BranchCreateDto dto = new BranchCreateDto("Updated", null, null);
        when(branchRepository.save(branch)).thenReturn(branch);
        when(branchMapper.toDto(branch)).thenReturn(
                new BranchResponseDto(branchId, "Updated", null, null)
        );

        BranchResponseDto result = branchService.updateBranch(branchId, dto, currentUser);

        assertEquals("Updated", result.name());
        assertTrue(branch.getUsers().contains(currentUser));
        verify(branchRepository).save(branch);
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

    @Test
    void getAllBranches_shouldReturnAll_whenNoSearch() {
        Pageable pageable = Pageable.ofSize(10);
        List<Branch> branches = List.of(Branch.builder().name("B1").build(), Branch.builder().name("B2").build());
        Page<Branch> page = new PageImpl<>(branches, pageable, branches.size());

        when(branchRepository.findAll(pageable)).thenReturn(page);
        when(branchMapper.toDto(any())).thenAnswer(invocation -> {
            Branch b = invocation.getArgument(0);
            return new BranchResponseDto(UUID.randomUUID(), b.getName(), null, null);
        });

        Page<BranchResponseDto> result = branchService.getAllBranches(null, pageable);

        assertEquals(2, result.getContent().size());
    }

    @Test
    void getAllBranches_shouldFilterBySearch() {
        Pageable pageable = Pageable.ofSize(10);
        List<Branch> branches = List.of(
                Branch.builder().name("B1").build(),
                Branch.builder().name("B2").build()
        );
        Page<Branch> page = new PageImpl<>(branches, pageable, branches.size());

        when(branchRepository.findByNameContainingIgnoreCase("B1", pageable)).thenReturn(
                new PageImpl<>(List.of(branches.get(0)), pageable, 1)
        );
        when(branchMapper.toDto(any())).thenAnswer(invocation -> {
            Branch b = invocation.getArgument(0);
            return new BranchResponseDto(UUID.randomUUID(), b.getName(), null, null);
        });

        Page<BranchResponseDto> result = branchService.getAllBranches("B1", pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("B1", result.getContent().get(0).name());
    }

    @Test
    void getBranchById_shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(branchRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> branchService.getBranchById(id));
    }

    @Test
    void getBranchById_shouldReturnBranch_whenFound() {
        UUID id = UUID.randomUUID();
        Branch branch = Branch.builder().name("B1").build();
        when(branchRepository.findById(id)).thenReturn(Optional.of(branch));
        when(branchMapper.toDto(branch)).thenReturn(new BranchResponseDto(id, "B1", null, null));

        BranchResponseDto result = branchService.getBranchById(id);
        assertEquals("B1", result.name());
    }

    @Test
    void getBranchByName_shouldThrow_whenNotFound() {
        when(branchRepository.findAll()).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> branchService.getBranchByName("B1"));
    }

    @Test
    void getBranchByName_shouldReturnBranch_whenFound() {
        Branch branch = Branch.builder().name("B1").build();
        when(branchRepository.findAll()).thenReturn(List.of(branch));
        when(branchMapper.toDto(branch)).thenReturn(new BranchResponseDto(UUID.randomUUID(), "B1", null, null));

        BranchResponseDto result = branchService.getBranchByName("B1");
        assertEquals("B1", result.name());
    }

    @Test
    void getBranchesForUser_shouldReturnAll_whenNoSearch() {
        Branch b1 = Branch.builder().name("B1").build();
        Branch b2 = Branch.builder().name("B2").build();
        currentUser.setBranches(new ArrayList<>(List.of(b1, b2)));

        Pageable pageable = Pageable.ofSize(10);
        when(branchMapper.toDtoList(any())).thenAnswer(invocation -> {
            List<Branch> list = invocation.getArgument(0);
            return list.stream()
                    .map(b -> new BranchResponseDto(UUID.randomUUID(), b.getName(), null, null))
                    .toList();
        });

        Page<BranchResponseDto> result = branchService.getBranchesForUser(currentUser, null, pageable);
        assertEquals(2, result.getContent().size());
    }

    @Test
    void getBranchesForUser_shouldFilterBySearch() {
        Branch b1 = Branch.builder().name("B1").build();
        Branch b2 = Branch.builder().name("B2").build();
        currentUser.setBranches(new ArrayList<>(List.of(b1, b2)));

        Pageable pageable = Pageable.ofSize(10);
        when(branchMapper.toDtoList(any())).thenAnswer(invocation -> {
            List<Branch> list = invocation.getArgument(0);
            return list.stream()
                    .map(b -> new BranchResponseDto(UUID.randomUUID(), b.getName(), null, null))
                    .toList();
        });

        Page<BranchResponseDto> result = branchService.getBranchesForUser(currentUser, "B2", pageable);
        assertEquals(1, result.getContent().size());
        assertEquals("B2", result.getContent().get(0).name());
    }

}