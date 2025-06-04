package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.DishSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishSizeRepository extends JpaRepository<DishSize, Long> {
}