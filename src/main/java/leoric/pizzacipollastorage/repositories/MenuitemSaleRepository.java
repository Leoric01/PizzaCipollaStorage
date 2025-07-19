package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.MenuItemSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MenuitemSaleRepository extends JpaRepository<MenuItemSale, UUID> {
}