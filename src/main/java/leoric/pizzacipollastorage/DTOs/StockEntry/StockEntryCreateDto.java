package leoric.pizzacipollastorage.DTOs.StockEntry;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class StockEntryCreateDto {
    private UUID ingredientId;
    private UUID supplierId;
    private float quantityReceived;
    private float pricePerUnitWithoutTax;
    private LocalDate receivedDate;
}