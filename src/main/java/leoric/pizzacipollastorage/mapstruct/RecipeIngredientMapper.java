package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.Pizza.RecipeIngredientShortDto;
import leoric.pizzacipollastorage.models.RecipeIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecipeIngredientMapper {

    @Mapping(source = "quantity", target = "amount")
    RecipeIngredientShortDto toShortDto(RecipeIngredient recipeIngredient);
}