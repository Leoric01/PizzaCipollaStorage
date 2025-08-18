package leoric.pizzacipollastorage.loans.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientLoanItemDto {

    @NotNull(message = "IngredientId is required")
    private UUID ingredientId;

    @DecimalMin(value = "0.1", inclusive = true, message = "Quantity must be greater than 0,1")
    private float quantity;
}