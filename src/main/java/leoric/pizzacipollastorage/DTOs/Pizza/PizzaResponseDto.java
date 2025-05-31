package leoric.pizzacipollastorage.DTOs.Pizza;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PizzaResponseDto {
    private Long id;
    private String name;
    private List<RecipeIngredientShortDto> ingredients;
}