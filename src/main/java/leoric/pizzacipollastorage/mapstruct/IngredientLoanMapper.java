package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.loans.dtos.IngredientLoanPatchDto;
import leoric.pizzacipollastorage.loans.dtos.IngredientLoanResponseDto;
import leoric.pizzacipollastorage.loans.models.IngredientLoan;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = IngredientLoanItemMapper.class)
public interface IngredientLoanMapper {

    IngredientLoanResponseDto toDto(IngredientLoan loan);

    List<IngredientLoanResponseDto> toDtoList(List<IngredientLoan> loans);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget IngredientLoan entity, IngredientLoanPatchDto dto);
}