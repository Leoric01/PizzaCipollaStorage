package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, UUID> {
    List<RecipeIngredient> findByMenuItemId(UUID menuItemId);

    void deleteAllByMenuItemId(UUID menuItemId);
//    Optional<RecipeIngredient> findByMenuItemIdAndIngredientIdAndDishSizeId(UUID id, UUID id1, UUID l);
}