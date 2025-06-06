package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    List<RecipeIngredient> findByPizzaId(Long pizzaId);

    Optional<RecipeIngredient> findByPizzaIdAndIngredientIdAndDishSizeId(Long id, Long id1, Long l);
}