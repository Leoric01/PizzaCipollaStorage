package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.Ingredient.IngredientAlias.IngredientAliasDto;
import leoric.pizzacipollastorage.mapstruct.IngredientAliasMapper;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.IngredientAlias;
import leoric.pizzacipollastorage.repositories.IngredientAliasRepository;
import leoric.pizzacipollastorage.repositories.IngredientRepository;
import leoric.pizzacipollastorage.services.interfaces.IngredientAliasService;
import leoric.pizzacipollastorage.utils.CustomUtilityString;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IngredientAliasServiceImpl implements IngredientAliasService {

    private final IngredientAliasRepository aliasRepository;
    private final IngredientRepository ingredientRepository;
    private final IngredientAliasMapper aliasMapper;

    @Override
    public IngredientAliasDto addAlias(IngredientAliasDto dto) {
        Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId())
                .orElseThrow(() -> new EntityNotFoundException("Ingredient " + dto.getIngredientId() + " not found"));

        String normalizedNewAlias = CustomUtilityString.normalize(dto.getAliasName());

        boolean alreadyExists = aliasRepository.findAllByIngredientId(ingredient.getId()).stream()
                .map(IngredientAlias::getAliasName)
                .map(CustomUtilityString::normalize)
                .anyMatch(a -> a.equals(normalizedNewAlias));

        if (alreadyExists) {
            return null;
        }

        IngredientAlias alias = IngredientAlias.builder()
                .aliasName(dto.getAliasName())
                .ingredient(ingredient)
                .build();

        alias = aliasRepository.save(alias);
        return aliasMapper.toDto(alias);
    }

    @Override
    public Optional<Ingredient> findIngredientByNameFlexible(String inputName) {
        String normalizedInput = CustomUtilityString.normalize(inputName);

        return ingredientRepository.findAll().stream()
                .filter(i -> CustomUtilityString.normalize(i.getName()).equals(normalizedInput))
                .findFirst()
                .or(() -> aliasRepository.findAll().stream()
                        .filter(a -> CustomUtilityString.normalize(a.getAliasName()).equals(normalizedInput))
                        .map(IngredientAlias::getIngredient)
                        .findFirst());
    }

    @Override
    public List<IngredientAliasDto> getAliasesByIngredientId(UUID ingredientId) {
        return aliasRepository.findAllByIngredientId(ingredientId).stream()
                .map(aliasMapper::toDto)
                .toList();
    }

    @Override
    public void deleteAlias(UUID aliasId) {
        aliasRepository.deleteById(aliasId);
    }
}