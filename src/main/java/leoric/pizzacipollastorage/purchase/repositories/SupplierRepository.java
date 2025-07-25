package leoric.pizzacipollastorage.purchase.repositories;

import leoric.pizzacipollastorage.purchase.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, UUID> {
    Optional<Supplier> findByNameIgnoreCase(String name);

}