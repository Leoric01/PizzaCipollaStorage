package leoric.pizzacipollastorage.loans.repositories;

import leoric.pizzacipollastorage.loans.models.IngredientLoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IngredientLoanRepository extends JpaRepository<IngredientLoan, UUID> {
    Page<IngredientLoan> findByBranchIdAndLoanTypeNameContainingIgnoreCase(UUID branchId, String search, Pageable pageable);

    Page<IngredientLoan> findByBranchId(UUID branchId, Pageable pageable);
}