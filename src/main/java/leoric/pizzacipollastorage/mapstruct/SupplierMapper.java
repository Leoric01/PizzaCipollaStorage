package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.Supplier.SupplierCreateDto;
import leoric.pizzacipollastorage.DTOs.Supplier.SupplierResponseDto;
import leoric.pizzacipollastorage.models.Supplier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierMapper {

    Supplier toEntity(SupplierCreateDto dto);

    SupplierResponseDto toDto(Supplier entity);
}