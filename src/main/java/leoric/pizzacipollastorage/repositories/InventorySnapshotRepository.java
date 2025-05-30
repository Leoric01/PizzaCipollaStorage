package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.InventorySnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventorySnapshotRepository extends JpaRepository<InventorySnapshot, Long> {
}