package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.Loans.IngredientLoanItemDto;
import leoric.pizzacipollastorage.models.IngredientLoanItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientLoanItemMapper {
    @Mapping(target = "ingredientName", source = "ingredient.name")
    IngredientLoanItemDto toDto(IngredientLoanItem item);

    List<IngredientLoanItemDto> toDtoList(List<IngredientLoanItem> items);
}