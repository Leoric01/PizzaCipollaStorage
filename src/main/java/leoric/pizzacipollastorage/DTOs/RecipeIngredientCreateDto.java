package leoric.pizzacipollastorage.DTOs;

import lombok.Data;

@Data
public class RecipeIngredientCreateDto {
    private Long pizzaId;
    private Long ingredientId;
    private float quantity;
}