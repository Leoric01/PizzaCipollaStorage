package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, UUID> {
    Optional<Pizza> findByName(String name);
}