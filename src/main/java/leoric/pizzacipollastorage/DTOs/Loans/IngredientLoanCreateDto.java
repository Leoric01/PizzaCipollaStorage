package leoric.pizzacipollastorage.DTOs.Loans;

import leoric.pizzacipollastorage.models.enums.LoanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientLoanCreateDto {
    private UUID fromBranchId;
    private UUID toBranchId;
    private LoanType loanType;
    private List<IngredientLoanItemDto> items;
}