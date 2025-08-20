package leoric.pizzacipollastorage.loans.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "LoanType is required")
    private LoanType loanType;

    private String note;

    private String counterpartyName;
    private String counterpartyContact;

    @NotEmpty(message = "At least one item is required")
    private List<@Valid IngredientLoanItemDto> items;
}