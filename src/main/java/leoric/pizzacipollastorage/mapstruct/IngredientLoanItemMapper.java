package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.loans.dtos.IngredientLoanItemDto;
import leoric.pizzacipollastorage.loans.dtos.IngredientLoanItemResponseDto;
import leoric.pizzacipollastorage.loans.models.IngredientLoanItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientLoanItemMapper {

    @Mapping(target = "ingredientId", expression = "java(item.getIngredient() != null ? item.getIngredient().getId() : null)")
    @Mapping(target = "ingredientName", expression = "java(item.getIngredient() != null ? item.getIngredient().getName() : null)")
    IngredientLoanItemResponseDto toResponseDto(IngredientLoanItem item);

    List<IngredientLoanItemResponseDto> toResponseDtoList(List<IngredientLoanItem> items);
}