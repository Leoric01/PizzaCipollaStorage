package leoric.pizzacipollastorage.vat.dtos.ProductCategory;

import leoric.pizzacipollastorage.vat.dtos.VatRateShortDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryResponseDto {
    private UUID id;
    private String name;
    private VatRateShortDto vatRate;
}