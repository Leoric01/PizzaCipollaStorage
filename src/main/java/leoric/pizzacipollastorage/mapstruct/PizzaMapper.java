package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientShortDto;
import leoric.pizzacipollastorage.DTOs.Pizza.PizzaResponseDto;
import leoric.pizzacipollastorage.DTOs.Pizza.RecipeIngredientShortDto;
import leoric.pizzacipollastorage.DTOs.Pizza.RecipeIngredientVeryShortDto;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.Pizza;
import leoric.pizzacipollastorage.models.RecipeIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {IngredientMapper.class})
public interface PizzaMapper {

    PizzaResponseDto toDto(Pizza pizza);

    List<PizzaResponseDto> toDtoList(List<Pizza> pizzas);

    @Mapping(source = "quantity", target = "amount")
    @Mapping(source = "ingredient", target = "ingredient")
    RecipeIngredientShortDto toShortDto(RecipeIngredient recipeIngredient);
    @Mapping(source = "quantity", target = "amount")
    @Mapping(source = "ingredient", target = "ingredient")
    RecipeIngredientVeryShortDto toVeryShortDto(RecipeIngredient recipeIngredient);
    IngredientShortDto toShortDto(Ingredient ingredient);
}