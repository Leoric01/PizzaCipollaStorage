package leoric.pizzacipollastorage.services;

import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientAlias.IngredientAliasOverviewDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientCreateDto;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientResponseDto;
import leoric.pizzacipollastorage.handler.exceptions.DuplicateIngredientNameException;
import leoric.pizzacipollastorage.mapstruct.IngredientMapper;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.IngredientAlias;
import leoric.pizzacipollastorage.models.ProductCategory;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.repositories.ProductCategoryRepository;
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
    private final IngredientMapper ingredientMapper;
    private final IngredientAliasService ingredientAliasService;
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public IngredientResponseDto createIngredient(IngredientCreateDto dto) {
        if (ingredientRepository.existsByName(dto.getName())) {
            throw new DuplicateIngredientNameException("Ingredient with name '" + dto.getName() + "' already exists");
        }

        ProductCategory category = productCategoryRepository
                .findByNameIgnoreCase(dto.getCategory())
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + dto.getCategory()));

        Ingredient ingredient = ingredientMapper.toEntity(dto);
        ingredient.setCategory(category);

        Ingredient saved = ingredientRepository.save(ingredient);
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