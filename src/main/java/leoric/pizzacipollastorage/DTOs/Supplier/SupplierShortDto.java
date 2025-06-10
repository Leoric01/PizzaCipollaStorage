package leoric.pizzacipollastorage.DTOs.Supplier;

import lombok.Data;

import java.util.UUID;

@Data
public class SupplierShortDto {
    private UUID id;
    private String name;
}