package leoric.pizzacipollastorage.vat.repositories;

import leoric.pizzacipollastorage.vat.models.VatRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VatRateRepository extends JpaRepository<VatRate, UUID> {

    boolean existsByName(String name);
//    Optional<VatRate> findByDefaultTrue();
}