package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientAlias.IngredientAliasDto;
import leoric.pizzacipollastorage.models.Ingredient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface IngredientAliasService {
    IngredientAliasDto addAlias(IngredientAliasDto aliasDto);

    List<IngredientAliasDto> getAliasesByIngredientId(UUID ingredientId);

    void deleteAlias(UUID aliasId);

    Optional<Ingredient> findIngredientByNameFlexible(String inputName);
}