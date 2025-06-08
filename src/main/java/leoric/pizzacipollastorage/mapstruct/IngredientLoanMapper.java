package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.Loans.IngredientLoanResponseDto;
import leoric.pizzacipollastorage.models.IngredientLoan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = IngredientLoanItemMapper.class)
public interface IngredientLoanMapper {

    @Mapping(target = "fromBranchName", source = "fromBranch.name")
    @Mapping(target = "toBranchName", source = "toBranch.name")
    IngredientLoanResponseDto toDto(IngredientLoan loan);

    List<IngredientLoanResponseDto> toDtoList(List<IngredientLoan> loans);
}