package leoric.pizzacipollastorage.loans.dtos;

import leoric.pizzacipollastorage.models.enums.LoanStatus;
import leoric.pizzacipollastorage.models.enums.LoanType;
import lombok.Data;

import java.util.List;

@Data
public class IngredientLoanPatchDto {
    private LoanStatus status;
    private LoanType loanType;
    private String note;
    private String counterpartyName;
    private String counterpartyContact;
    private List<IngredientLoanItemDto> items;
}