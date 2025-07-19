package leoric.pizzacipollastorage.purchase.repositories;

import leoric.pizzacipollastorage.purchase.models.PurchaseInvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PurchaseInvoiceItemRepository extends JpaRepository<PurchaseInvoiceItem, UUID> {
}