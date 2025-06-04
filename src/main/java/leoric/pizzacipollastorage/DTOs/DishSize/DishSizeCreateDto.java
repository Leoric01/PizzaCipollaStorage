package leoric.pizzacipollastorage.DTOs.DishSize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishSizeCreateDto {
    private String name;
    private float factor;
}