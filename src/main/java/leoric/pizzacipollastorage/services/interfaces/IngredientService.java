package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientAlias.IngredientAliasOverviewDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientCreateDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientResponseDto;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.enums.InventoryStatus;
import leoric.pizzacipollastorage.purchase.models.PurchaseOrder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface IngredientService {
    IngredientResponseDto ingredientCreate(UUID branchId, IngredientCreateDto dto);

    IngredientResponseDto ingredientUpdate(UUID branchId, UUID id, IngredientCreateDto dto);

    List<IngredientResponseDto> ingredientGetAll(UUID branchId);

    void ingredientDeleteById(UUID branchId, UUID id);

    InventoryStatus checkInventoryStatus(Ingredient ingredient);

    Optional<IngredientAliasOverviewDto> getAliasOverviewByName(String inputName);

    PurchaseOrder generateAutoPurchaseOrder();

    List<IngredientResponseDto> ingredientCreateBulk(UUID branchId, List<IngredientCreateDto> dtos);

    IngredientResponseDto ingredientGetById(UUID ingredientId);
}