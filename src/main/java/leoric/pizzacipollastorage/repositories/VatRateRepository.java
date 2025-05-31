package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.VatRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VatRateRepository extends JpaRepository<VatRate, Long> {

    boolean existsByName(String name);

}