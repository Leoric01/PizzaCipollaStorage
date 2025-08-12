package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.MenuItemCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuItemCategoryRepository extends JpaRepository<MenuItemCategory, UUID> {
    Optional<MenuItemCategory> findByNameIgnoreCaseAndBranchId(String name, UUID branchId);

    List<MenuItemCategory> findAllByBranchId(UUID branchId);

    Page<MenuItemCategory> findByBranchIdAndNameContainingIgnoreCase(UUID branchId, String search, Pageable pageable);

    Page<MenuItemCategory> findByBranchId(UUID branchId, Pageable pageable);
}