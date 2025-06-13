package leoric.pizzacipollastorage.purchase.dtos.Supplier;

import lombok.Data;

import java.util.UUID;

@Data
public class SupplierResponseDto {
    private UUID id;
    private String name;
    private String contactInfo;
}