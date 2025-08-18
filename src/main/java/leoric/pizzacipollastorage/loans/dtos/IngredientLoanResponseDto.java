package leoric.pizzacipollastorage.loans.dtos;

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

    private LoanType loanType;
    private LoanStatus status;

    private LocalDate createdAt;
    private LocalDate returnedAt;

    private String note;

    private String counterpartyName;
    private String counterpartyContact;

    private List<IngredientLoanItemResponseDto> items;
}