package leoric.pizzacipollastorage.purchase.dtos.Supplier;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SupplierCreateDto {
    @NotBlank(message = "name musi byt vyplneno")
    private String name;
    private String contactInfo;
}