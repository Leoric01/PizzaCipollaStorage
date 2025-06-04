package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.PurchaseInvoice.PurchaseInvoiceItemDto;
import leoric.pizzacipollastorage.DTOs.PurchaseInvoice.PurchaseInvoiceResponseDto;
import leoric.pizzacipollastorage.models.PurchaseInvoice;
import leoric.pizzacipollastorage.models.PurchaseInvoiceItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseInvoiceMapper {

    @Mapping(target = "supplier", source = "supplier")
    @Mapping(target = "items", source = "items")
    PurchaseInvoiceResponseDto toDto(PurchaseInvoice invoice);

    @Mapping(target = "ingredient", source = "ingredient")
    @Mapping(target = "vatRate", source = "vatRate")
    PurchaseInvoiceItemDto toItemDto(PurchaseInvoiceItem item);
}