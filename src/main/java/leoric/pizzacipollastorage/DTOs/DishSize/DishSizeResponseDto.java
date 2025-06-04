package leoric.pizzacipollastorage.DTOs.DishSize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DishSizeResponseDto {
    private Long id;
    private String name;
    private float factor;
}