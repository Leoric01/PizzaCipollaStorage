package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.IngredientLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IngredientLoanRepository extends JpaRepository<IngredientLoan, UUID> {
}