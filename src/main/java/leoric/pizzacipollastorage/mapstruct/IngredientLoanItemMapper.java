package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.loans.dtos.IngredientLoanItemDto;
import leoric.pizzacipollastorage.loans.models.IngredientLoanItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientLoanItemMapper {

    IngredientLoanItemDto toDto(IngredientLoanItem item);

    List<IngredientLoanItemDto> toDtoList(List<IngredientLoanItem> items);
}