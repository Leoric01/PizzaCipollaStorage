package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.StockEntry.StockEntryCreateDto;
import leoric.pizzacipollastorage.DTOs.StockEntry.StockEntryResponseDto;
import leoric.pizzacipollastorage.models.StockEntry;
import leoric.pizzacipollastorage.purchase.SupplierMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {IngredientMapper.class, SupplierMapper.class, UUIDMapper.class})
public interface StockEntryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ingredient", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    StockEntry toEntity(StockEntryCreateDto dto);

    StockEntryResponseDto toDto(StockEntry stockEntry);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget StockEntry entity, StockEntryCreateDto dto);
}