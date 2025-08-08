package leoric.pizzacipollastorage.purchase.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.purchase.SupplierMapper;
import leoric.pizzacipollastorage.purchase.dtos.Supplier.SupplierCreateDto;
import leoric.pizzacipollastorage.purchase.dtos.Supplier.SupplierResponseDto;
import leoric.pizzacipollastorage.purchase.models.Supplier;
import leoric.pizzacipollastorage.purchase.repositories.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final BranchRepository branchRepository;
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    public SupplierResponseDto supplierCreate(UUID branchId, SupplierCreateDto dto) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        Supplier supplier = supplierMapper.toEntity(dto);
        supplier.setBranch(branch);

        return supplierMapper.toDto(supplierRepository.save(supplier));
    }

    @Override
    public List<SupplierResponseDto> supplierCreateBulk(UUID branchId, List<SupplierCreateDto> dtos) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        List<Supplier> suppliers = dtos.stream()
                .map(supplierMapper::toEntity)
                .peek(s -> s.setBranch(branch))
                .toList();

        return supplierRepository.saveAll(suppliers).stream()
                .map(supplierMapper::toDto)
                .toList();
    }

    @Override
    public List<SupplierResponseDto> supplierGetAll(UUID branchId) {
        return supplierRepository.findAllByBranchId(branchId).stream()
                .map(supplierMapper::toDto)
                .toList();
    }


    @Override
    public SupplierResponseDto supplierGetById(UUID branchId, UUID supplierId) {
        Supplier supplier = getSupplierInBranchOrThrow(supplierId, branchId);
        return supplierMapper.toDto(supplier);
    }

    @Override
    public SupplierResponseDto supplierUpdate(UUID branchId, UUID supplierId, SupplierCreateDto dto) {
        Supplier supplier = getSupplierInBranchOrThrow(supplierId, branchId);
        supplier.setName(dto.getName());
        supplier.setContactInfo(dto.getContactInfo());
        return supplierMapper.toDto(supplierRepository.save(supplier));
    }

    @Override
    public void supplierDelete(UUID branchId, UUID supplierId) {
        Supplier supplier = getSupplierInBranchOrThrow(supplierId, branchId);
        supplierRepository.delete(supplier);
    }

    private Branch getBranchOrThrow(UUID branchId) {
        return branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));
    }

    private Supplier getSupplierInBranchOrThrow(UUID supplierId, UUID branchId) {
        return supplierRepository.findByIdAndBranchId(supplierId, branchId)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found in branch"));
    }

}