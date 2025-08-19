package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.loans.dtos.IngredientLoanCreateDto;
import leoric.pizzacipollastorage.loans.dtos.IngredientLoanPatchDto;
import leoric.pizzacipollastorage.loans.dtos.IngredientLoanResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface IngredientLoanService {
    IngredientLoanResponseDto getLoanById(UUID branchId, UUID id);

    IngredientLoanResponseDto createLoan(UUID branchId, IngredientLoanCreateDto dto);

    IngredientLoanResponseDto markLoanAsReturned(UUID branchId, UUID id);

    Page<IngredientLoanResponseDto> getAllLoans(UUID branchId, String search, Pageable pageable);

    IngredientLoanResponseDto patchLoan(UUID branchId, UUID id, IngredientLoanPatchDto dto);

    IngredientLoanResponseDto markLoanAsCancelled(UUID branchId, UUID loanId);

    void deleteLoan(UUID branchId, UUID id);
}