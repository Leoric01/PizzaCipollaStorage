package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.IngredientCreateDto;
import leoric.pizzacipollastorage.DTOs.IngredientResponseDto;
import leoric.pizzacipollastorage.handler.exceptions.DuplicateIngredientNameException;
import leoric.pizzacipollastorage.mapstruct.IngredientMapper;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.VatRate;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.repositories.VatRateRepository;
import leoric.pizzacipollastorage.services.interfaces.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;
    private final VatRateRepository vatRateRepository;
    private final IngredientMapper ingredientMapper;

    @Override
    public IngredientResponseDto createIngredient(IngredientCreateDto dto) {
        boolean exists = ingredientRepository.existsByName(dto.getName());
        if (exists) {
            throw new DuplicateIngredientNameException("Ingredient with name '" + dto.getName() + "' already exists");
        }

        VatRate vatRate = vatRateRepository.findById(dto.getVatRateId())
                .orElseThrow(() -> new EntityNotFoundException("Vat rate not found"));

        Ingredient entity = ingredientMapper.toEntity(dto);
        entity.setVatRate(vatRate);

        Ingredient saved = ingredientRepository.save(entity);

        return ingredientMapper.toDto(saved);
    }

    @Override
    public List<IngredientResponseDto> getAllIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredientMapper.toDtoList(ingredients);
    }
}