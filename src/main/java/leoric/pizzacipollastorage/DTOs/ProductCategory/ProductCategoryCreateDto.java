package leoric.pizzacipollastorage.DTOs.ProductCategory;

import leoric.pizzacipollastorage.vat.dtos.VatRateShortDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryCreateDto {
    private String name;
    private VatRateShortDto vatRate;
}