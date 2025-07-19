package leoric.pizzacipollastorage.purchase.services;

import leoric.pizzacipollastorage.purchase.dtos.Supplier.SupplierCreateDto;
import leoric.pizzacipollastorage.purchase.dtos.Supplier.SupplierResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SupplierService {
    SupplierResponseDto createSupplier(SupplierCreateDto dto);
    List<SupplierResponseDto> getAllSuppliers();
}