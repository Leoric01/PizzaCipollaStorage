package leoric.pizzacipollastorage.purchase;

import leoric.pizzacipollastorage.purchase.dtos.PurchaseInvoice.PurchaseInvoiceItemDto;
import leoric.pizzacipollastorage.purchase.dtos.PurchaseInvoice.PurchaseInvoiceResponseDto;
import leoric.pizzacipollastorage.purchase.models.PurchaseInvoice;
import leoric.pizzacipollastorage.purchase.models.PurchaseInvoiceItem;
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