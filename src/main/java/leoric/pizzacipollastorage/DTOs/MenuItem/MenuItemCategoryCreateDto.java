package leoric.pizzacipollastorage.DTOs.MenuItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class MenuItemCategoryCreateDto {
    private String name;
}