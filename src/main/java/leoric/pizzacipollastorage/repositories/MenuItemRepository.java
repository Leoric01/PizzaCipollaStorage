package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {
    Optional<MenuItem> findByNameIgnoreCaseAndBranchId(String name, UUID branchId);

    @Query("SELECT m FROM MenuItem m JOIN m.recipeIngredients ri WHERE ri.ingredient.id = :ingredientId")
    List<MenuItem> findAllByRecipeIngredientsIngredientId(@Param("ingredientId") UUID ingredientId);

    List<MenuItem> findAllByBranchId(UUID branchId);

    Optional<MenuItem> findByIdAndBranchId(UUID id, UUID branchId);

    Page<MenuItem> findByBranchId(UUID branchId, Pageable pageable);

    Page<MenuItem> findByBranchIdAndNameContainingIgnoreCase(UUID branchId, String name, Pageable pageable);
}