package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.MenuItem.RecipeIngredientShortDto;
import leoric.pizzacipollastorage.models.RecipeIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = IngredientMapper.class)
public interface RecipeIngredientMapper {

    @Mapping(source = "quantity", target = "quantity")
    RecipeIngredientShortDto toShortDto(RecipeIngredient recipeIngredient);
}