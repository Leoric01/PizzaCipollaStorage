package leoric.pizzacipollastorage.services;

import leoric.pizzacipollastorage.DTOs.PurchaseOrder.PurchaseOrderCreateDto;
import leoric.pizzacipollastorage.DTOs.PurchaseOrder.PurchaseOrderResponseDto;
import leoric.pizzacipollastorage.mapstruct.PurchaseOrderMapper;
import leoric.pizzacipollastorage.models.PurchaseOrder;
import leoric.pizzacipollastorage.models.Supplier;
import leoric.pizzacipollastorage.models.enums.OrderStatus;
import leoric.pizzacipollastorage.repositories.PurchaseOrderRepository;
import leoric.pizzacipollastorage.repositories.SupplierRepository;
import leoric.pizzacipollastorage.services.interfaces.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository supplierRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;

    @Override
    public PurchaseOrderResponseDto toDto(PurchaseOrder order) {
        return purchaseOrderMapper.toDto(order);
    }

    @Override
    public PurchaseOrder save(PurchaseOrder order) {
        return purchaseOrderRepository.save(order);
    }

    @Override
    public List<PurchaseOrderResponseDto> getAll() {
        List<PurchaseOrder> orders = purchaseOrderRepository.findAll();
        return purchaseOrderMapper.toDtoList(orders);
    }

    @Override
    public Optional<PurchaseOrderResponseDto> getById(UUID id) {
        return purchaseOrderRepository.findById(id)
                .map(purchaseOrderMapper::toDto);
    }

    @Override
    public PurchaseOrder create(PurchaseOrderCreateDto dto) {
        PurchaseOrder order = purchaseOrderMapper.toEntity(dto);

        if (dto.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                    .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));
            order.setSupplier(supplier);
        } else {
            throw new IllegalArgumentException("SupplierId is required");
        }

        if (order.getItems() != null) {
            order.getItems().forEach(item -> item.setPurchaseOrder(order));
        }

        return purchaseOrderRepository.save(order);
    }

    @Override
    public void updateStatus(UUID id, OrderStatus status) {
        PurchaseOrder order = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PurchaseOrder not found"));
        order.setStatus(status);
        purchaseOrderRepository.save(order);
    }
}