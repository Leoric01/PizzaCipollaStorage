package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.StockEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StockEntryRepository extends JpaRepository<StockEntry, UUID> {
    void deleteAllByPurchaseInvoiceItemId(UUID itemId);
}