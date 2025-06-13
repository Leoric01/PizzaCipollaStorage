package leoric.pizzacipollastorage.DTOs.MenuItem;

import lombok.Data;

@Data
public class MenuItemCreateDto {
    private String name;
    private String description;
    private MenuItemCategoryCreateDto menuItemCategory;
}