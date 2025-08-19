package leoric.pizzacipollastorage.loans.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.handler.BusinessErrorCodes;
import leoric.pizzacipollastorage.handler.exceptions.BusinessException;
import leoric.pizzacipollastorage.handler.exceptions.NotAuthorizedForBranchException;
import leoric.pizzacipollastorage.inventory.services.InventoryService;
import leoric.pizzacipollastorage.loans.dtos.IngredientLoanCreateDto;
import leoric.pizzacipollastorage.loans.dtos.IngredientLoanItemDto;
import leoric.pizzacipollastorage.loans.dtos.IngredientLoanPatchDto;
import leoric.pizzacipollastorage.loans.dtos.IngredientLoanResponseDto;
import leoric.pizzacipollastorage.loans.models.IngredientLoan;
import leoric.pizzacipollastorage.loans.models.IngredientLoanItem;
import leoric.pizzacipollastorage.loans.repositories.IngredientLoanRepository;
import leoric.pizzacipollastorage.mapstruct.IngredientLoanMapper;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.enums.LoanStatus;
import leoric.pizzacipollastorage.models.enums.LoanType;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.services.interfaces.IngredientLoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class IngredientLoanServiceImpl implements IngredientLoanService {

    private final IngredientLoanRepository loanRepository;
    private final BranchRepository branchRepository;
    private final InventoryService inventoryService;
    private final IngredientRepository ingredientRepository;

    private final IngredientLoanMapper ingredientLoanMapper;

    @Override
    public IngredientLoanResponseDto getLoanById(UUID branchId, UUID id) {
        IngredientLoan ingredientLoan = loanRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("IngredientLoan not found"));
        if (!ingredientLoan.getBranch().getId().equals(branchId)) {
            throw new BusinessException(BusinessErrorCodes.NOT_AUTHORIZED_FOR_BRANCH);
        }
        return ingredientLoanMapper.toDto(ingredientLoan);
    }

    @Override
    @Transactional
    public IngredientLoanResponseDto createLoan(UUID branchId, IngredientLoanCreateDto dto) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        if (!branch.getId().equals(dto.getBranchId())) {
            throw new NotAuthorizedForBranchException("Can not create loan to this branch");
        }

        IngredientLoan loan = new IngredientLoan();
        loan.setBranch(branch);
        loan.setLoanType(dto.getLoanType());
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setCreatedAt(LocalDate.now());

        List<IngredientLoanItem> items = new ArrayList<>();

        for (IngredientLoanItemDto itemDto : dto.getItems()) {
            Ingredient ingredient = ingredientRepository.findById(itemDto.getIngredientId())
                    .orElseThrow(() -> new EntityNotFoundException("Ingredient not found: " + itemDto.getIngredientId()));

            if (!ingredient.getBranch().getId().equals(branchId)) {
                throw new BusinessException(BusinessErrorCodes.INGREDIENT_NOT_IN_BRANCH);
            }

            if (itemDto.getQuantity() <= 0) {
                throw new BusinessException(BusinessErrorCodes.MISSING_INGREDIENT_QUANTITY);
            }

            IngredientLoanItem loanItem = new IngredientLoanItem();
            loanItem.setIngredient(ingredient);
            loanItem.setQuantity(itemDto.getQuantity());
            loanItem.setIngredientLoan(loan);

            items.add(loanItem);

            if (dto.getLoanType() == LoanType.OUT) {
                inventoryService.addToInventory(branchId, ingredient.getId(), -itemDto.getQuantity());
            } else if (dto.getLoanType() == LoanType.IN) {
                inventoryService.addToInventory(branchId, ingredient.getId(), itemDto.getQuantity());
            }
        }

        loan.setItems(items);

        IngredientLoan saved = loanRepository.save(loan);
        return ingredientLoanMapper.toDto(saved);
    }

    @Override
    @Transactional
    public IngredientLoanResponseDto patchLoan(UUID branchId, UUID id, IngredientLoanPatchDto dto) {
        IngredientLoan loan = loanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found"));

        if (!loan.getBranch().getId().equals(branchId)) {
            throw new NotAuthorizedForBranchException("Can not modify loan from this branch");
        }

        LoanStatus oldStatus = loan.getStatus();
        LoanStatus newStatus = dto.getStatus();

        if (newStatus != null && newStatus != oldStatus) {
            loan.setStatus(newStatus);
            log.info("Loan [{}] status changed: {} → {}", loan.getId(), oldStatus, newStatus);
        }

        if (dto.getNote() != null) {
            loan.setNote(dto.getNote());
        }

        if (dto.getLoanType() != null) {
            loan.setLoanType(dto.getLoanType());
        }

        // update items if provided
        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            Map<UUID, IngredientLoanItem> existingItems = loan.getItems().stream()
                    .collect(Collectors.toMap(i -> i.getIngredient().getId(), i -> i));

            for (IngredientLoanItemDto itemDto : dto.getItems()) {
                Ingredient ingredient = ingredientRepository.findById(itemDto.getIngredientId())
                        .orElseThrow(() -> new EntityNotFoundException("Ingredient not found: " + itemDto.getIngredientId()));

                if (!ingredient.getBranch().getId().equals(branchId)) {
                    throw new BusinessException(BusinessErrorCodes.INGREDIENT_NOT_IN_BRANCH);
                }

                if (itemDto.getQuantity() <= 0) {
                    throw new BusinessException(BusinessErrorCodes.MISSING_INGREDIENT_QUANTITY);
                }

                IngredientLoanItem existingItem = existingItems.get(itemDto.getIngredientId());
                if (existingItem != null) {
                    float oldQty = existingItem.getQuantity();
                    float newQty = itemDto.getQuantity();
                    float delta = newQty - oldQty;

                    existingItem.setQuantity(newQty);

                    if (loan.getLoanType() == LoanType.OUT) {
                        inventoryService.addToInventory(branchId, ingredient.getId(), -delta);
                    } else if (loan.getLoanType() == LoanType.IN) {
                        inventoryService.addToInventory(branchId, ingredient.getId(), delta);
                    }

                } else {
                    IngredientLoanItem newItem = new IngredientLoanItem();
                    newItem.setIngredient(ingredient);
                    newItem.setQuantity(itemDto.getQuantity());
                    newItem.setIngredientLoan(loan);
                    loan.getItems().add(newItem);

                    if (loan.getLoanType() == LoanType.OUT) {
                        inventoryService.addToInventory(branchId, ingredient.getId(), -itemDto.getQuantity());
                    } else if (loan.getLoanType() == LoanType.IN) {
                        inventoryService.addToInventory(branchId, ingredient.getId(), itemDto.getQuantity());
                    }
                }
            }
        }

        return ingredientLoanMapper.toDto(loanRepository.save(loan));
    }


    @Override
    @Transactional
    public IngredientLoanResponseDto markLoanAsReturned(UUID branchId, UUID loanId) {
        IngredientLoan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found"));

        if (!loan.getBranch().getId().equals(branchId)) {
            throw new NotAuthorizedForBranchException("Loan does not belong to this branch");
        }

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new BusinessException(BusinessErrorCodes.LOAN_ALREADY_RETURNED);
        }

        for (IngredientLoanItem item : loan.getItems()) {
            Ingredient ingredient = item.getIngredient();

            if (!ingredient.getBranch().getId().equals(branchId)) {
                throw new BusinessException(BusinessErrorCodes.INGREDIENT_NOT_IN_BRANCH);
            }

            float qty = item.getQuantity();

            if (qty <= 0) {
                throw new BusinessException(BusinessErrorCodes.MISSING_INGREDIENT_QUANTITY);
            }

            if (loan.getLoanType() == LoanType.OUT) {
                inventoryService.addToInventory(branchId, ingredient.getId(), qty);
            } else if (loan.getLoanType() == LoanType.IN) {
                inventoryService.addToInventory(branchId, ingredient.getId(), -qty);
            }
        }

        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnedAt(LocalDate.now());

        IngredientLoan saved = loanRepository.save(loan);
        return ingredientLoanMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IngredientLoanResponseDto> getAllLoans(UUID branchId, String search, Pageable pageable) {
        Page<IngredientLoan> page;

        if (search != null && !search.isBlank()) {
            page = loanRepository.findByBranchIdAndLoanTypeNameContainingIgnoreCase(branchId, search, pageable);
        } else {
            page = loanRepository.findByBranchId(branchId, pageable);
        }

        return page.map(ingredientLoanMapper::toDto);
    }

    @Override
    @Transactional
    public IngredientLoanResponseDto markLoanAsCancelled(UUID branchId, UUID loanId) {
        IngredientLoan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found"));

        if (!loan.getBranch().getId().equals(branchId)) {
            throw new NotAuthorizedForBranchException("Loan does not belong to this branch");
        }

        if (loan.getStatus() == LoanStatus.CANCELLED) {
            throw new BusinessException(BusinessErrorCodes.LOAN_ALREADY_CANCELLED);
        }

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new BusinessException(BusinessErrorCodes.LOAN_ALREADY_RETURNED);
        }

        for (IngredientLoanItem item : loan.getItems()) {
            Ingredient ingredient = item.getIngredient();

            if (!ingredient.getBranch().getId().equals(branchId)) {
                throw new BusinessException(BusinessErrorCodes.INGREDIENT_NOT_IN_BRANCH);
            }

            float qty = item.getQuantity();

            if (qty <= 0) {
                throw new BusinessException(BusinessErrorCodes.MISSING_INGREDIENT_QUANTITY);
            }

            if (loan.getLoanType() == LoanType.OUT) {
                inventoryService.addToInventory(branchId, ingredient.getId(), qty);
            } else if (loan.getLoanType() == LoanType.IN) {
                inventoryService.addToInventory(branchId, ingredient.getId(), -qty);
            }
        }

        loan.setStatus(LoanStatus.CANCELLED);

        IngredientLoan saved = loanRepository.save(loan);
        return ingredientLoanMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteLoan(UUID branchId, UUID loanId) {
        IngredientLoan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found"));

        if (!loan.getBranch().getId().equals(branchId)) {
            throw new NotAuthorizedForBranchException("Loan does not belong to this branch");
        }

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new BusinessException(BusinessErrorCodes.LOAN_ALREADY_RETURNED);
        }

        for (IngredientLoanItem item : loan.getItems()) {
            Ingredient ingredient = item.getIngredient();

            if (!ingredient.getBranch().getId().equals(branchId)) {
                throw new BusinessException(BusinessErrorCodes.INGREDIENT_NOT_IN_BRANCH);
            }

            float qty = item.getQuantity();
            if (qty <= 0) {
                throw new BusinessException(BusinessErrorCodes.MISSING_INGREDIENT_QUANTITY);
            }

            if (loan.getLoanType() == LoanType.OUT) {
                inventoryService.addToInventory(branchId, ingredient.getId(), qty);
            } else if (loan.getLoanType() == LoanType.IN) {
                inventoryService.addToInventory(branchId, ingredient.getId(), -qty);
            }
        }

        loanRepository.delete(loan);
    }
}