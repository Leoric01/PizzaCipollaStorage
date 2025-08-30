package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleLastTimestampResponseDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleLastTimestampUpdateDto;
import leoric.pizzacipollastorage.DTOs.MenuItemSale.MenuItemSaleLastTimestampUpsertDto;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.models.MenuItemSaleLastTimestamp;
import leoric.pizzacipollastorage.repositories.MenuItemSaleLastTimestampRepository;
import leoric.pizzacipollastorage.services.interfaces.MenuItemSaleLastTimestampService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuItemSaleLastTimestampServiceImpl implements MenuItemSaleLastTimestampService {

    private final MenuItemSaleLastTimestampRepository menuItemSaleLastTimestampRepository;
    private final BranchRepository branchRepository;

    @Override
    public MenuItemSaleLastTimestampResponseDto saleTimestampGetByBranch(UUID branchId) {
        MenuItemSaleLastTimestamp entity = menuItemSaleLastTimestampRepository.findByBranchId(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Sale timestamp not found for branch " + branchId + ", probably wasn't created yet"));
        return toDto(entity);
    }

    @Override
    public MenuItemSaleLastTimestampResponseDto saleTimestampCreate(UUID branchId, MenuItemSaleLastTimestampUpsertDto dto) {
        if (menuItemSaleLastTimestampRepository.existsByBranchId(branchId)) {
            throw new IllegalStateException("Sale timestamp already exists for branch " + branchId);
        }

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found: " + branchId));

        MenuItemSaleLastTimestamp entity = MenuItemSaleLastTimestamp.builder()
                .branch(branch)
                .lastSaleTimestamp(dto.getLastSaleTimestamp())
                .build();

        MenuItemSaleLastTimestamp saved = menuItemSaleLastTimestampRepository.save(entity);
        return toDto(saved);
    }

    @Override
    public MenuItemSaleLastTimestampResponseDto saleTimestampUpdate(UUID branchId, MenuItemSaleLastTimestampUpdateDto dto) {
        MenuItemSaleLastTimestamp entity = menuItemSaleLastTimestampRepository.findByBranchId(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Sale timestamp not found for branch " + branchId));

        entity.setLastSaleTimestamp(dto.getLastSaleTimestamp());
        MenuItemSaleLastTimestamp updated = menuItemSaleLastTimestampRepository.save(entity);
        return toDto(updated);
    }

    @Override
    public MenuItemSaleLastTimestampResponseDto saleTimestampUpsert(UUID branchId, MenuItemSaleLastTimestampUpsertDto dto) {
        MenuItemSaleLastTimestamp entity = menuItemSaleLastTimestampRepository.findByBranchId(branchId)
                .orElseGet(() -> {
                    Branch branch = branchRepository.findById(branchId)
                            .orElseThrow(() -> new EntityNotFoundException("Branch not found: " + branchId));
                    return MenuItemSaleLastTimestamp.builder()
                            .branch(branch)
                            .build();
                });

        entity.setLastSaleTimestamp(dto.getLastSaleTimestamp());
        MenuItemSaleLastTimestamp saved = menuItemSaleLastTimestampRepository.save(entity);
        return toDto(saved);
    }

    private MenuItemSaleLastTimestampResponseDto toDto(MenuItemSaleLastTimestamp entity) {
        return MenuItemSaleLastTimestampResponseDto.builder()
                .id(entity.getId())
                .branchId(entity.getBranch().getId())
                .lastSaleTimestamp(entity.getLastSaleTimestamp())
                .build();
    }
}