package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.InventorySnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface InventorySnapshotRepository extends JpaRepository<InventorySnapshot, Long> {
    boolean existsByIngredientAndTimestampBetween(Ingredient ingredient, LocalDateTime from, LocalDateTime to);
    Optional<InventorySnapshot> findTopByIngredientOrderByTimestampDesc(Ingredient ingredient);

}