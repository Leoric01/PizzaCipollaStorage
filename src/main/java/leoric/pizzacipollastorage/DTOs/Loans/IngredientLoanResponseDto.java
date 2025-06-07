package leoric.pizzacipollastorage.DTOs.Loans;

import leoric.pizzacipollastorage.models.enums.LoanStatus;
import leoric.pizzacipollastorage.models.enums.LoanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientLoanResponseDto {
    private Long id;
    private String fromBranchName;
    private String toBranchName;
    private LoanType type;
    private LoanStatus status;
    private LocalDate createdAt;
    private List<IngredientLoanItemDto> items;
}