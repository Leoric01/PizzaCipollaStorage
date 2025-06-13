package leoric.pizzacipollastorage.purchase;

import leoric.pizzacipollastorage.purchase.dtos.Supplier.SupplierCreateDto;
import leoric.pizzacipollastorage.purchase.dtos.Supplier.SupplierResponseDto;
import leoric.pizzacipollastorage.purchase.models.Supplier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierMapper {

    Supplier toEntity(SupplierCreateDto dto);

    SupplierResponseDto toDto(Supplier entity);
}