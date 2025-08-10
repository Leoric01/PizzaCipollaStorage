package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {
    boolean existsByNameIgnoreCaseAndBranchId(String name, UUID branchId);

    Optional<Ingredient> findByNameIgnoreCaseAndBranchId(String name, UUID branchId);
    List<Ingredient> findAllByBranchId(UUID branchId);
    Optional<Ingredient> findByNameIgnoreCase(String name);
}