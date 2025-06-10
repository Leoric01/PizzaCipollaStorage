package leoric.pizzacipollastorage.DTOs.Ingredient.IngredientAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientAliasDto {
    private UUID id;
    private String aliasName;
    private UUID ingredientId;
}