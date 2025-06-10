package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {
    boolean existsByName(String name);
    Optional<Ingredient> findByNameIgnoreCase(String name);
}