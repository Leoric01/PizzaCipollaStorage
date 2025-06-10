package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.Loans.IngredientLoanCreateDto;
import leoric.pizzacipollastorage.DTOs.Loans.IngredientLoanResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface IngredientLoanService {
    IngredientLoanResponseDto createLoan(IngredientLoanCreateDto dto);

    IngredientLoanResponseDto markLoanAsReturned(UUID loanId);
    List<IngredientLoanResponseDto> getAllLoans();
}