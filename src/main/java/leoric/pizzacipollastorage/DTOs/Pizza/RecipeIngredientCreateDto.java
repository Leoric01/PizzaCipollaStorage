package leoric.pizzacipollastorage.DTOs.Pizza;

import lombok.Data;

@Data
public class RecipeIngredientCreateDto {
    private Long pizzaId;
    private Long ingredientId;
    private float quantity;
}