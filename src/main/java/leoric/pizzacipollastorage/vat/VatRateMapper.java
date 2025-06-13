package leoric.pizzacipollastorage.vat;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientCreateDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientResponseDto;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.vat.dtos.VatRateShortDto;
import leoric.pizzacipollastorage.vat.models.VatRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {VatRateMapper.class})
public interface VatRateMapper {
    VatRateShortDto toShortDto(VatRate vatRate);

    @Mapping(target = "category", ignore = true)
    Ingredient toEntity(IngredientCreateDto dto);

    IngredientResponseDto toDto(Ingredient ingredient);
    List<VatRateShortDto> toShortDtoList(List<VatRate> vatRates);
}