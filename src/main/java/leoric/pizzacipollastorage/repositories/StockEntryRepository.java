package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.StockEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockEntryRepository extends JpaRepository<StockEntry, Long> {
}