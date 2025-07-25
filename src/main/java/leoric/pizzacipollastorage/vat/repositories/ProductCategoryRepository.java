package leoric.pizzacipollastorage.vat.repositories;

import leoric.pizzacipollastorage.vat.models.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {
    Optional<ProductCategory> findByNameIgnoreCaseAndBranchId(String name, UUID branchId);

    List<ProductCategory> findAllByBranchId(UUID branchId);
}