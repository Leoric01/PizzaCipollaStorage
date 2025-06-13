package leoric.pizzacipollastorage.DTOs.PurchaseOrder;

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
public class PurchaseOrderCreateDto {
    private UUID supplierId;
    private LocalDate orderDate;
    private String note;
    private List<PurchaseOrderItemCreateDto> items;
}