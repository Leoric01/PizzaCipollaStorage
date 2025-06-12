package leoric.pizzacipollastorage.DTOs.MenuItem;

import leoric.pizzacipollastorage.models.enums.MenuItemCategory;
import lombok.Data;

@Data
public class MenuItemCreateDto {
    private String name;
    private String description;
    private MenuItemCategory category;
}