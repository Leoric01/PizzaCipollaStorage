package leoric.pizzacipollastorage.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientShortDto {
    private Long id;
    private String name;
    private String unit;
}