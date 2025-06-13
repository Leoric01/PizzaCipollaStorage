package leoric.pizzacipollastorage.purchase.services;

import leoric.pizzacipollastorage.models.enums.OrderStatus;
import leoric.pizzacipollastorage.purchase.dtos.PurchaseOrder.PurchaseOrderCreateDto;
import leoric.pizzacipollastorage.purchase.dtos.PurchaseOrder.PurchaseOrderResponseDto;
import leoric.pizzacipollastorage.purchase.models.PurchaseOrder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface PurchaseOrderService {
    PurchaseOrder save(PurchaseOrder order);

    List<PurchaseOrderResponseDto> getAll();

    Optional<PurchaseOrderResponseDto> getById(UUID id);

    PurchaseOrder create(PurchaseOrderCreateDto dto);

    void updateStatus(UUID id, OrderStatus status);

    PurchaseOrderResponseDto toDto(PurchaseOrder saved);
}