package leoric.pizzacipollastorage.DTOs.StockEntry;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockEntryCreateDto {
    private Long ingredientId;
    private Long supplierId;
    private float quantityReceived;
    private float pricePerUnitWithoutTax;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime receivedDate;
}