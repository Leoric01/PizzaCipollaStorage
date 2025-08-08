package leoric.pizzacipollastorage.purchase.services;

import jakarta.validation.Valid;
import leoric.pizzacipollastorage.purchase.dtos.Supplier.SupplierCreateDto;
import leoric.pizzacipollastorage.purchase.dtos.Supplier.SupplierResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface SupplierService {
    SupplierResponseDto supplierCreate(UUID branchId, SupplierCreateDto dto);

    List<SupplierResponseDto> supplierGetAll(UUID branchId);

    List<SupplierResponseDto> supplierCreateBulk(UUID branchId, List<SupplierCreateDto> dtos);

    SupplierResponseDto supplierGetById(UUID branchId, UUID supplierId);

    SupplierResponseDto supplierUpdate(UUID branchId, UUID supplierId, @Valid SupplierCreateDto dto);

    void supplierDelete(UUID branchId, UUID supplierId);

}