package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientCreateDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientInventoryDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientResponseDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientShortDto;
import leoric.pizzacipollastorage.DTOs.ProductCategory.ProductCategoryResponseDto;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.vat.dtos.VatRateShortDto;
import leoric.pizzacipollastorage.vat.models.ProductCategory;
import leoric.pizzacipollastorage.vat.models.VatRate;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    @Mapping(target = "productCategory", source = "category")
    IngredientResponseDto toDto(Ingredient ingredient);

    @Mapping(target = "category", ignore = true)
    Ingredient toEntity(IngredientCreateDto dto);

    VatRateShortDto toShortDto(VatRate vatRate);

    List<IngredientResponseDto> toDtoList(List<Ingredient> ingredients);

    @Mapping(target = "measuredQuantity", ignore = true)
    IngredientInventoryDto toInventoryDto(Ingredient ingredient);

    @Mapping(target = "vatRate", source = "vatRate")
    ProductCategoryResponseDto toDto(ProductCategory category);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "unit", source = "unit")
    IngredientShortDto toShortDto(Ingredient ingredient);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    void update(@MappingTarget Ingredient entity, IngredientCreateDto dto);

}