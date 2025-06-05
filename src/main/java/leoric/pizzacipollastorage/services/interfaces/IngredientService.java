package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientAlias.IngredientAliasOverviewDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientCreateDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IngredientService {
    IngredientResponseDto createIngredient(IngredientCreateDto dto);

    List<IngredientResponseDto> getAllIngredients();
    Optional<IngredientAliasOverviewDto> getAliasOverviewByName(String inputName);
}