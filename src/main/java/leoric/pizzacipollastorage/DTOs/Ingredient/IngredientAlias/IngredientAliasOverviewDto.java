package leoric.pizzacipollastorage.DTOs.Ingredient.IngredientAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientAliasOverviewDto {
    private UUID id;
    private String name;
    private List<String> aliases;
}