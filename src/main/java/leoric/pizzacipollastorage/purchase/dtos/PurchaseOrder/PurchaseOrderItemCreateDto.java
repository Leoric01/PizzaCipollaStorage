package leoric.pizzacipollastorage.purchase.dtos.PurchaseOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderItemCreateDto {
    private UUID ingredientId;
    private float quantityOrdered;
    private String note;
}