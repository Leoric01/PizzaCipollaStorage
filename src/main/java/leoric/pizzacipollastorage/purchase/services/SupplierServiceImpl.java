package leoric.pizzacipollastorage.purchase.services;

import leoric.pizzacipollastorage.purchase.SupplierMapper;
import leoric.pizzacipollastorage.purchase.dtos.Supplier.SupplierCreateDto;
import leoric.pizzacipollastorage.purchase.dtos.Supplier.SupplierResponseDto;
import leoric.pizzacipollastorage.purchase.models.Supplier;
import leoric.pizzacipollastorage.purchase.repositories.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    public SupplierResponseDto createSupplier(SupplierCreateDto dto) {
        Supplier supplier = supplierMapper.toEntity(dto);
        return supplierMapper.toDto(supplierRepository.save(supplier));
    }

    @Override
    public List<SupplierResponseDto> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(supplierMapper::toDto)
                .collect(Collectors.toList());
    }
}