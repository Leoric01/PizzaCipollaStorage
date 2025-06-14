package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.loans.dtos.IngredientLoanPatchDto;
import leoric.pizzacipollastorage.loans.dtos.IngredientLoanResponseDto;
import leoric.pizzacipollastorage.loans.models.IngredientLoan;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = IngredientLoanItemMapper.class)
public interface IngredientLoanMapper {

    @Mapping(target = "fromBranchName", source = "fromBranch.name")
    @Mapping(target = "toBranchName", source = "toBranch.name")
    IngredientLoanResponseDto toDto(IngredientLoan loan);

    List<IngredientLoanResponseDto> toDtoList(List<IngredientLoan> loans);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget IngredientLoan entity, IngredientLoanPatchDto dto);
}