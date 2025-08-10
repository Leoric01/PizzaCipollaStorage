package leoric.pizzacipollastorage.purchase.repositories;

import leoric.pizzacipollastorage.purchase.models.PurchaseInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PurchaseInvoiceRepository extends JpaRepository<PurchaseInvoice, UUID> {
    List<PurchaseInvoice> findTop10ByOrderByIssuedDateDesc();

    List<PurchaseInvoice> findAllByBranchIdOrderByIssuedDateDesc(UUID branchId);

    Optional<PurchaseInvoice> findByIdAndBranchId(UUID id, UUID branchId);


}