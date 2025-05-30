package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.IngredientCreateDto;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.VatRate;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.repositories.VatRateRepository;
import leoric.pizzacipollastorage.services.interfaces.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;
    private final VatRateRepository vatRateRepository;

    public Ingredient createIngredient(IngredientCreateDto dto) {
        VatRate vatRate = vatRateRepository.findById(dto.getVatRateId())
                .orElseThrow(() -> new EntityNotFoundException("Vat rate not found"));

        Ingredient ingredient = new Ingredient();
        ingredient.setName(dto.getName());
        ingredient.setUnit(dto.getUnit());
        ingredient.setLossCleaningFactor(dto.getLossCleaningFactor());
        ingredient.setLossUsageFactor(dto.getLossUsageFactor());
        ingredient.setVatRate(vatRate);
        return ingredientRepository.save(ingredient);
    }
}