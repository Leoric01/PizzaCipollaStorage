package leoric.pizzacipollastorage.loans.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientLoanItemDto {
    private String ingredientName;
    private float quantity;
}