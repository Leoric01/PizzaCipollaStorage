package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.StockEntry.StockEntryCreateDto;
import leoric.pizzacipollastorage.DTOs.StockEntry.StockEntryResponseDto;
import leoric.pizzacipollastorage.models.StockEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {IngredientMapper.class, SupplierMapper.class, UUIDMapper.class})
public interface StockEntryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ingredient", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    StockEntry toEntity(StockEntryCreateDto dto);

    StockEntryResponseDto toDto(StockEntry stockEntry);
}