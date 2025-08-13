package leoric.pizzacipollastorage.vat.repositories;

import leoric.pizzacipollastorage.vat.models.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {
    Optional<ProductCategory> findByNameIgnoreCaseAndBranchId(String name, UUID branchId);

    List<ProductCategory> findAllByBranchId(UUID branchId);

    List<ProductCategory> findAllByNameInIgnoreCaseAndBranchId(List<String> names, UUID branchId);

    Page<ProductCategory> findByBranchIdAndNameContainingIgnoreCase(UUID branchId, String search, Pageable pageable);

    Page<ProductCategory> findByBranchId(UUID branchId, Pageable pageable);
}