package leoric.pizzacipollastorage.inventory;

import leoric.pizzacipollastorage.inventory.dtos.Inventory.InventorySnapshotResponseDto;
import leoric.pizzacipollastorage.inventory.models.InventorySnapshot;
import leoric.pizzacipollastorage.mapstruct.IngredientMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {IngredientMapper.class})
public interface InventorySnapshotMapper {

    @Mapping(target = "discrepancy", expression = "java(snapshot.getDiscrepancy())")
    InventorySnapshotResponseDto toDto(InventorySnapshot snapshot);

    InventorySnapshot toEntity(InventorySnapshotResponseDto dto);
}