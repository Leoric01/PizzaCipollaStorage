package leoric.pizzacipollastorage.DTOs.Loans;

import leoric.pizzacipollastorage.models.enums.LoanStatus;
import leoric.pizzacipollastorage.models.enums.LoanType;
import lombok.Data;

@Data
public class IngredientLoanPatchDto {
    private LoanStatus status;
    private LoanType loanType;
    private String note;
}