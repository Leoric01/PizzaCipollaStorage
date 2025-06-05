package leoric.pizzacipollastorage.DTOs.Ingredient.IngredientAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientAliasDto {
    private Long id;
    private String aliasName;
    private Long ingredientId;
}