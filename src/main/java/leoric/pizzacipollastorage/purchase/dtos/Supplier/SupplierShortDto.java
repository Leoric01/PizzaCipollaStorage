package leoric.pizzacipollastorage.purchase.dtos.Supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierShortDto {
    private UUID id;
    private String name;
}