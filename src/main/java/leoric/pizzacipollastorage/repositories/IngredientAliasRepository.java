package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.IngredientAlias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IngredientAliasRepository extends JpaRepository<IngredientAlias, UUID> {
    List<IngredientAlias> findAllByIngredientId(UUID ingredientId);
    Optional<IngredientAlias> findByAliasNameIgnoreCase(String aliasName);
}