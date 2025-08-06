package leoric.pizzacipollastorage.vat.dtos.ProductCategory;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryCreateBulkDto {
    @NotEmpty(message = "Seznam kategorií nesmí být prázdný")
    @Valid
    private List<ProductCategoryCreateDto> categories;
}