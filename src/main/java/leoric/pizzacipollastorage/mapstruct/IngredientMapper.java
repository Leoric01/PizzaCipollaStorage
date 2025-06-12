package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientCreateDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientInventoryDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientResponseDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientShortDto;
import leoric.pizzacipollastorage.DTOs.Vat.VatRateShortDto;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.VatRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientMapper {
    IngredientResponseDto toDto(Ingredient ingredient);

    Ingredient toEntity(IngredientCreateDto dto);

    VatRateShortDto toShortDto(VatRate vatRate);

    List<IngredientResponseDto> toDtoList(List<Ingredient> ingredients);

    @Mapping(target = "measuredQuantity", ignore = true)
    IngredientInventoryDto toInventoryDto(Ingredient ingredient);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "unit", source = "unit")
    IngredientShortDto toShortDto(Ingredient ingredient);

}