package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.Loans.IngredientLoanCreateDto;
import leoric.pizzacipollastorage.DTOs.Loans.IngredientLoanResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IngredientLoanService {
    IngredientLoanResponseDto createLoan(IngredientLoanCreateDto dto);
    IngredientLoanResponseDto markLoanAsReturned(Long loanId);
    List<IngredientLoanResponseDto> getAllLoans();
}