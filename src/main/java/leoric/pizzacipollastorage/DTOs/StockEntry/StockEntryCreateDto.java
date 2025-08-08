package leoric.pizzacipollastorage.DTOs.StockEntry;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class StockEntryCreateDto {
    private UUID ingredientId;
    private UUID supplierId;
    private float quantityReceived;
    private float pricePerUnitWithoutTax;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate receivedDate;
}