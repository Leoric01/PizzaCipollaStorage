package leoric.pizzacipollastorage.DTOs.StockEntry;

import com.fasterxml.jackson.annotation.JsonFormat;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientShortDto;
import leoric.pizzacipollastorage.purchase.dtos.Supplier.SupplierShortDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class StockEntryResponseDto {
    private UUID id;
    private IngredientShortDto ingredient;
    private SupplierShortDto supplier;
    private float quantityReceived;
    private float pricePerUnitWithoutTax;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate receivedDate;
}