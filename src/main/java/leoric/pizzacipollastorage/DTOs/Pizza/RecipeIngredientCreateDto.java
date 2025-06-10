package leoric.pizzacipollastorage.DTOs.Pizza;

import lombok.Data;

import java.util.UUID;

@Data
public class RecipeIngredientCreateDto {
    private UUID pizzaId;
    private UUID ingredientId;
    private float quantity;
}