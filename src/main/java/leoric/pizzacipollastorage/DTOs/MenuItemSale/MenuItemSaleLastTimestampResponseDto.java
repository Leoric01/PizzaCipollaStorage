package leoric.pizzacipollastorage.DTOs.MenuItemSale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemSaleLastTimestampResponseDto {
    private UUID id;
    private UUID branchId;
    private LocalDateTime lastSaleTimestamp;
}