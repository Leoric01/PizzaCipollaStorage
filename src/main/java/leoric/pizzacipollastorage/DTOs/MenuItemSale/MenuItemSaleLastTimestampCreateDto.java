package leoric.pizzacipollastorage.DTOs.MenuItemSale;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemSaleLastTimestampCreateDto {
    @NotNull(message = "lastSaleTimestamp is mandatory attribute")
    private LocalDateTime lastSaleTimestamp;
}