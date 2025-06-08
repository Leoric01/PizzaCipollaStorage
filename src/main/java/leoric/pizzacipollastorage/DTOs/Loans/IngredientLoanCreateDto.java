package leoric.pizzacipollastorage.DTOs.Loans;

import leoric.pizzacipollastorage.models.enums.LoanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientLoanCreateDto {
    private Long fromBranchId;
    private Long toBranchId;
    private LoanType loanType;
    private List<IngredientLoanItemDto> items;
}