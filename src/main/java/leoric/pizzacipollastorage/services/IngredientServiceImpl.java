package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientAlias.IngredientAliasOverviewDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientCreateDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientResponseDto;
import leoric.pizzacipollastorage.handler.exceptions.DuplicateIngredientNameException;
import leoric.pizzacipollastorage.mapstruct.IngredientMapper;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.IngredientAlias;
import leoric.pizzacipollastorage.models.VatRate;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.repositories.VatRateRepository;
import leoric.pizzacipollastorage.services.interfaces.IngredientAliasService;
import leoric.pizzacipollastorage.services.interfaces.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;
    private final VatRateRepository vatRateRepository;
    private final IngredientMapper ingredientMapper;
    private final IngredientAliasService ingredientAliasService;

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

    @Override
    public Optional<IngredientAliasOverviewDto> getAliasOverviewByName(String inputName) {
        return ingredientAliasService.findIngredientByNameFlexible(inputName)
                .map(ingredient -> IngredientAliasOverviewDto.builder()
                        .id(ingredient.getId())
                        .name(ingredient.getName())
                        .aliases(
                                ingredient.getAliases() != null
                                        ? ingredient.getAliases().stream()
                                        .map(IngredientAlias::getAliasName)
                                        .distinct()
                                        .toList()
                                        : List.of()
                        )
                        .build());
    }
}