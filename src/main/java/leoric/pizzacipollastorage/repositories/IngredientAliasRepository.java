package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.IngredientAlias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientAliasRepository extends JpaRepository<IngredientAlias, Long> {
    List<IngredientAlias> findAllByIngredientId(Long ingredientId);
    Optional<IngredientAlias> findByAliasNameIgnoreCase(String aliasName);
}