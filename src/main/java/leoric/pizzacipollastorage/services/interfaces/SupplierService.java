package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.Supplier.SupplierCreateDto;
import leoric.pizzacipollastorage.DTOs.Supplier.SupplierResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SupplierService {
    SupplierResponseDto createSupplier(SupplierCreateDto dto);
    List<SupplierResponseDto> getAllSuppliers();
}