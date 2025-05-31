package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.IngredientShortDto;
import leoric.pizzacipollastorage.DTOs.PizzaResponseDto;
import leoric.pizzacipollastorage.DTOs.RecipeIngredientShortDto;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.Pizza;
import leoric.pizzacipollastorage.models.RecipeIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PizzaMapper {

    PizzaResponseDto toDto(Pizza pizza);

    List<PizzaResponseDto> toDtoList(List<Pizza> pizzas);

    @Mapping(source = "ingredient", target = "ingredient")
    RecipeIngredientShortDto toShortDto(RecipeIngredient recipeIngredient);

    IngredientShortDto toShortDto(Ingredient ingredient);
}