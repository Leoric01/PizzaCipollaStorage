package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.PurchaseInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseInvoiceRepository extends JpaRepository<PurchaseInvoice, Long> {
    List<PurchaseInvoice> findTop10ByOrderByIssuedDateDesc();
}