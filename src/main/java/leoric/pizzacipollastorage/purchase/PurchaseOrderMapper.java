package leoric.pizzacipollastorage.purchase;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientShortDto;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.purchase.dtos.PurchaseOrder.PurchaseOrderCreateDto;
import leoric.pizzacipollastorage.purchase.dtos.PurchaseOrder.PurchaseOrderItemCreateDto;
import leoric.pizzacipollastorage.purchase.dtos.PurchaseOrder.PurchaseOrderItemResponseDto;
import leoric.pizzacipollastorage.purchase.dtos.PurchaseOrder.PurchaseOrderResponseDto;
import leoric.pizzacipollastorage.purchase.dtos.Supplier.SupplierShortDto;
import leoric.pizzacipollastorage.purchase.models.PurchaseOrder;
import leoric.pizzacipollastorage.purchase.models.PurchaseOrderItem;
import leoric.pizzacipollastorage.purchase.models.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper {

    PurchaseOrderResponseDto toDto(PurchaseOrder order);

    List<PurchaseOrderResponseDto> toDtoList(List<PurchaseOrder> orders);

    @Mapping(target = "supplier.id", source = "supplierId")
    @Mapping(target = "items", source = "items")
    PurchaseOrder toEntity(PurchaseOrderCreateDto dto);

    List<PurchaseOrder> toEntityList(List<PurchaseOrderCreateDto> dtos);

    @Mapping(target = "ingredient.id", source = "ingredientId")
    PurchaseOrderItem toEntity(PurchaseOrderItemCreateDto dto);

    PurchaseOrderItemResponseDto toDto(PurchaseOrderItem item);

    List<PurchaseOrderItemResponseDto> toItemDtoList(List<PurchaseOrderItem> items);

    default SupplierShortDto map(Supplier supplier) {
        if (supplier == null) return null;
        return SupplierShortDto.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .build();
    }

    default IngredientShortDto map(Ingredient ingredient) {
        if (ingredient == null) return null;
        return new IngredientShortDto(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getUnit()
        );
    }
}