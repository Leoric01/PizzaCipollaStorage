package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.IngredientCreateDto;
import leoric.pizzacipollastorage.DTOs.IngredientResponseDto;
import leoric.pizzacipollastorage.DTOs.VatRateShortDto;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.VatRate;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientMapper {
    IngredientResponseDto toDto(Ingredient ingredient);

    Ingredient toEntity(IngredientCreateDto dto);

    VatRateShortDto toShortDto(VatRate vatRate);

    List<IngredientResponseDto> toDtoList(List<Ingredient> ingredients);

}