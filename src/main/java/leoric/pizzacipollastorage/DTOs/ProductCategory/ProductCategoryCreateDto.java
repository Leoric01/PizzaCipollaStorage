package leoric.pizzacipollastorage.DTOs.ProductCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryCreateDto {
    @NotBlank(message = "chybi name pro novou product category")
    private String name;

    @NotNull(message = "vatId musi byt vyplneno")
    private UUID vatId;
}