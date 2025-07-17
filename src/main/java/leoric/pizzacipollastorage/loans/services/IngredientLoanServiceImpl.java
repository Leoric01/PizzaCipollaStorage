package leoric.pizzacipollastorage.loans.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.inventory.services.InventoryService;
import leoric.pizzacipollastorage.loans.dtos.IngredientLoanCreateDto;
import leoric.pizzacipollastorage.loans.dtos.IngredientLoanPatchDto;
import leoric.pizzacipollastorage.loans.dtos.IngredientLoanResponseDto;
import leoric.pizzacipollastorage.loans.models.Branch;
import leoric.pizzacipollastorage.loans.models.IngredientLoan;
import leoric.pizzacipollastorage.loans.models.IngredientLoanItem;
import leoric.pizzacipollastorage.loans.repositories.BranchRepository;
import leoric.pizzacipollastorage.loans.repositories.IngredientLoanRepository;
import leoric.pizzacipollastorage.mapstruct.IngredientLoanMapper;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.enums.LoanStatus;
import leoric.pizzacipollastorage.models.enums.LoanType;
import leoric.pizzacipollastorage.services.interfaces.IngredientAliasService;
import leoric.pizzacipollastorage.services.interfaces.IngredientLoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IngredientLoanServiceImpl implements IngredientLoanService {

    private final IngredientLoanRepository loanRepository;
    private final BranchRepository branchRepository;

    private final InventoryService inventoryService;
    private final IngredientAliasService ingredientAliasService;

    private final IngredientLoanMapper ingredientLoanMapper;

    @Override
    public IngredientLoanResponseDto createLoan(IngredientLoanCreateDto dto) {
        Branch from = branchRepository.findById(dto.getFromBranchId())
                .orElseThrow(() -> new EntityNotFoundException("Branch (from) not found"));
        Branch to = branchRepository.findById(dto.getToBranchId())
                .orElseThrow(() -> new EntityNotFoundException("Branch (to) not found"));

        IngredientLoan loan = new IngredientLoan();
        loan.setFromBranch(from);
        loan.setToBranch(to);
        loan.setLoanType(dto.getLoanType());
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setCreatedAt(LocalDate.now());

        List<IngredientLoanItem> items = dto.getItems().stream().map(itemDto -> {
            Ingredient ingredient = ingredientAliasService.findIngredientByNameFlexible(itemDto.getIngredientName())
                    .orElseThrow(() -> new EntityNotFoundException("Ingredient not found: " + itemDto.getIngredientName()));
            return IngredientLoanItem.builder()
                    .ingredient(ingredient)
                    .quantity(itemDto.getQuantity())
                    .ingredientLoan(loan)
                    .build();
        }).toList();

        loan.setItems(items);

        for (IngredientLoanItem item : items) {
            UUID ingredientId = item.getIngredient().getId();
            float qty = item.getQuantity();

            if (dto.getLoanType() == LoanType.OUT) {
                inventoryService.addToInventory(ingredientId, -qty);
            } else if (dto.getLoanType() == LoanType.IN) {
                inventoryService.addToInventory(ingredientId, qty);
            }
        }

        return ingredientLoanMapper.toDto(loanRepository.save(loan));
    }

    @Override
    public IngredientLoanResponseDto markLoanAsReturned(UUID loanId) {
        IngredientLoan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found"));

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new IllegalStateException("Loan already returned");
        }

        for (IngredientLoanItem item : loan.getItems()) {
            UUID ingredientId = item.getIngredient().getId();
            float qty = item.getQuantity();

            if (loan.getLoanType() == LoanType.OUT) {
                inventoryService.addToInventory(ingredientId, qty);
            } else if (loan.getLoanType() == LoanType.IN) {
                inventoryService.addToInventory(ingredientId, -qty);
            }
        }

        loan.setStatus(LoanStatus.RETURNED);
        return ingredientLoanMapper.toDto(loanRepository.save(loan));
    }

    @Override
    public IngredientLoanResponseDto patchLoan(UUID id, IngredientLoanPatchDto dto) {
        IngredientLoan loan = loanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found"));
        LoanStatus oldStatus = loan.getStatus();
        LoanStatus newStatus = dto.getStatus();
        ingredientLoanMapper.update(loan, dto);

        if (newStatus != null && newStatus != oldStatus) {
            log.info("Loan [{}] branchAccessRequestStatus changed: {} → {}", loan.getId(), oldStatus, newStatus);
        }
        return ingredientLoanMapper.toDto(loanRepository.save(loan));
    }

    @Override
    public List<IngredientLoanResponseDto> getAllLoans() {
        return loanRepository.findAll().stream().map(ingredientLoanMapper::toDto).toList();
    }
}