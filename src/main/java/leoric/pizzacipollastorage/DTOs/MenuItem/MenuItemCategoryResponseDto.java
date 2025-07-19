package leoric.pizzacipollastorage.DTOs.MenuItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class MenuItemCategoryResponseDto {
    private UUID id;
    private String name;
}