package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.IngredientCreateDto;
import leoric.pizzacipollastorage.DTOs.IngredientResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IngredientService {
    IngredientResponseDto createIngredient(IngredientCreateDto dto);

    List<IngredientResponseDto> getAllIngredients();
}