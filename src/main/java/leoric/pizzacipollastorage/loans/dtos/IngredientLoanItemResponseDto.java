package leoric.pizzacipollastorage.loans.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientLoanItemResponseDto {
    private UUID ingredientId;
    private String ingredientName;
    private Float quantity;
}