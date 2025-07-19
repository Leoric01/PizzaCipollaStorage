package leoric.pizzacipollastorage.purchase.dtos.PurchaseOrder;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientShortDto;
import leoric.pizzacipollastorage.models.enums.OrderItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderItemResponseDto {
    private UUID id;
    private IngredientShortDto ingredient;
    private float quantityOrdered;
    private float quantityReceived;
    private OrderItemStatus status;
    private String note;
}