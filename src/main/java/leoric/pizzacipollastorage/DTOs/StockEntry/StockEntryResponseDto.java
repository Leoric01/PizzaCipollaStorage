package leoric.pizzacipollastorage.DTOs.StockEntry;

import com.fasterxml.jackson.annotation.JsonFormat;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientShortDto;
import leoric.pizzacipollastorage.DTOs.Supplier.SupplierShortDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockEntryResponseDto {
    private Long id;
    private IngredientShortDto ingredient;
    private SupplierShortDto supplier;
    private float quantityReceived;
    private float pricePerUnitWithoutTax;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime receivedDate;
}