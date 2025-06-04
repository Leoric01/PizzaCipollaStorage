package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.PizzaSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PizzaSaleRepository extends JpaRepository<PizzaSale, Long> {
}