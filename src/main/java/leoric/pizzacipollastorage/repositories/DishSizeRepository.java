package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.DishSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DishSizeRepository extends JpaRepository<DishSize, UUID> {
    Optional<DishSize> findByDefaultSizeTrue();
}