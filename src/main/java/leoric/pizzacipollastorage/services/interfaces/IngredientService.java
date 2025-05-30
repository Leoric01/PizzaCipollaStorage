package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.IngredientCreateDto;
import leoric.pizzacipollastorage.models.Ingredient;
import org.springframework.stereotype.Service;

@Service
public interface IngredientService {
    Ingredient createIngredient(IngredientCreateDto dto);
}