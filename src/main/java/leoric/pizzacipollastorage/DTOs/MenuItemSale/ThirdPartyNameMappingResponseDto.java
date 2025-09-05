package leoric.pizzacipollastorage.DTOs.MenuItemSale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThirdPartyNameMappingResponseDto {
    private Map<String, UUID> mapped;
    private List<String> unmapped;
    private List<String> ignored;
}