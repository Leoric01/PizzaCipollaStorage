package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.Loans.IngredientLoanResponseDto;
import leoric.pizzacipollastorage.models.IngredientLoan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IngredientLoanMapper {
    IngredientLoanResponseDto toDto(IngredientLoan loan);
}