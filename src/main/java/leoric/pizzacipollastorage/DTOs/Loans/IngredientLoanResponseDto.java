package leoric.pizzacipollastorage.DTOs.Loans;

import leoric.pizzacipollastorage.models.enums.LoanStatus;
import leoric.pizzacipollastorage.models.enums.LoanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientLoanResponseDto {
    private UUID id;
    private String fromBranchName;
    private String toBranchName;
    private LoanType loanType;
    private LoanStatus status;
    private LocalDate createdAt;
    private List<IngredientLoanItemDto> items;
}