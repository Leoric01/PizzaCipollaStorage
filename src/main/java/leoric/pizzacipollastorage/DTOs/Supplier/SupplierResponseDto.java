package leoric.pizzacipollastorage.DTOs.Supplier;

import lombok.Data;

@Data
public class SupplierResponseDto {
    private Long id;
    private String name;
    private String contactInfo;
}