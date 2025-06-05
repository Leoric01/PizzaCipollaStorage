package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientAlias.IngredientAliasDto;
import leoric.pizzacipollastorage.models.IngredientAlias;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IngredientAliasMapper {
    @Mapping(source = "ingredient.id", target = "ingredientId")
    IngredientAliasDto toDto(IngredientAlias alias);
    IngredientAlias toEntity(IngredientAliasDto dto);
}