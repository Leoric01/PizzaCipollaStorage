package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientAlias.IngredientAliasOverviewDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientCreateDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientResponseDto;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.PurchaseOrder;
import leoric.pizzacipollastorage.models.enums.InventoryStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface IngredientService {
    IngredientResponseDto createIngredient(IngredientCreateDto dto);

    InventoryStatus checkInventoryStatus(Ingredient ingredient);
    List<IngredientResponseDto> getAllIngredients();
    Optional<IngredientAliasOverviewDto> getAliasOverviewByName(String inputName);

    PurchaseOrder generateAutoPurchaseOrder();

    IngredientResponseDto updateIngredient(UUID id, IngredientCreateDto dto);
}