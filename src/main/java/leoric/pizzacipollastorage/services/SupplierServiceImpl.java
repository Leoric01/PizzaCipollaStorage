package leoric.pizzacipollastorage.services;

import leoric.pizzacipollastorage.DTOs.Supplier.SupplierCreateDto;
import leoric.pizzacipollastorage.DTOs.Supplier.SupplierResponseDto;
import leoric.pizzacipollastorage.mapstruct.SupplierMapper;
import leoric.pizzacipollastorage.models.Supplier;
import leoric.pizzacipollastorage.repositories.SupplierRepository;
import leoric.pizzacipollastorage.services.interfaces.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public SupplierResponseDto createSupplier(SupplierCreateDto dto) {
        Supplier supplier = supplierMapper.toEntity(dto);
        return supplierMapper.toDto(supplierRepository.save(supplier));
    }

    public List<SupplierResponseDto> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(supplierMapper::toDto)
                .collect(Collectors.toList());
    }
}