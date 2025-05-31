package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientCreateDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientResponseDto;
import leoric.pizzacipollastorage.DTOs.Vat.VatRateShortDto;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.VatRate;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {VatRateMapper.class})
public interface VatRateMapper {
    VatRateShortDto toShortDto(VatRate vatRate);
    Ingredient toEntity(IngredientCreateDto dto);
    IngredientResponseDto toDto(Ingredient ingredient);
    List<VatRateShortDto> toShortDtoList(List<VatRate> vatRates);
}