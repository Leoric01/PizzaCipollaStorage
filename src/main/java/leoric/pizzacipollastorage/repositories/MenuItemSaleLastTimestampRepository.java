package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.MenuItemSaleLastTimestamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MenuItemSaleLastTimestampRepository extends JpaRepository<MenuItemSaleLastTimestamp, UUID> {
    Optional<MenuItemSaleLastTimestamp> findByBranchId(UUID branchId);

    boolean existsByBranchId(UUID branchId);
}