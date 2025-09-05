package leoric.pizzacipollastorage.inventory.repositories;

import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.inventory.models.InventorySnapshot;
import leoric.pizzacipollastorage.models.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventorySnapshotRepository extends JpaRepository<InventorySnapshot, UUID> {
    boolean existsByIngredientAndTimestampBetween(Ingredient ingredient, LocalDateTime from, LocalDateTime to);

    Optional<InventorySnapshot> findTopByIngredientOrderByTimestampDesc(Ingredient ingredient);

    Optional<InventorySnapshot> findTopByIngredientIdAndBranchIdOrderByTimestampDesc(UUID id, UUID branchId);

    Optional<InventorySnapshot> findTopByIngredientAndBranchOrderByTimestampDesc(Ingredient ingredient, Branch branch);

    Page<InventorySnapshot> findByBranchIdAndIngredientNameContainingIgnoreCase(UUID branchId, String search, Pageable pageable);

    Page<InventorySnapshot> findByBranchId(UUID branchId, Pageable pageable);

    List<InventorySnapshot> findByBranchId(UUID branchId);

    @Query("""
            SELECT s FROM InventorySnapshot s
            WHERE s.branch.id = :branchId
              AND (:search IS NULL OR LOWER(s.ingredient.name) LIKE LOWER(CONCAT('%', :search, '%')))
              AND s.timestamp = (
                  SELECT MAX(s2.timestamp)
                  FROM InventorySnapshot s2
                  WHERE s2.ingredient.id = s.ingredient.id AND s2.branch.id = :branchId
              )
            """)
    Page<InventorySnapshot> findLatestPerIngredient(
            @Param("branchId") UUID branchId,
            @Param("search") String search,
            Pageable pageable
    );
}