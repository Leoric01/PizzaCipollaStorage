package leoric.pizzacipollastorage.DTOs.PurchaseOrder;

import leoric.pizzacipollastorage.DTOs.Supplier.SupplierShortDto;
import leoric.pizzacipollastorage.models.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderResponseDto {
    private UUID id;
    private SupplierShortDto supplier;
    private LocalDate orderDate;
    private OrderStatus status;
    private String note;
    private List<PurchaseOrderItemResponseDto> items;
}