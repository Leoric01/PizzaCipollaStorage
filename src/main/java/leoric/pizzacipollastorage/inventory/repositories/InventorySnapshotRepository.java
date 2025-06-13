package leoric.pizzacipollastorage.inventory.repositories;

import leoric.pizzacipollastorage.inventory.models.InventorySnapshot;
import leoric.pizzacipollastorage.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventorySnapshotRepository extends JpaRepository<InventorySnapshot, UUID> {
    boolean existsByIngredientAndTimestampBetween(Ingredient ingredient, LocalDateTime from, LocalDateTime to);
    Optional<InventorySnapshot> findTopByIngredientOrderByTimestampDesc(Ingredient ingredient);

    Optional<InventorySnapshot> findTopByIngredientIdOrderByTimestampDesc(UUID id);
}