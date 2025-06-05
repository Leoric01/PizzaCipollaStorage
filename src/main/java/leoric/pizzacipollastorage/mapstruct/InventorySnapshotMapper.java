package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.Inventory.InventorySnapshotResponseDto;
import leoric.pizzacipollastorage.models.InventorySnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {IngredientMapper.class})
public interface InventorySnapshotMapper {

    @Mapping(target = "discrepancy", expression = "java(snapshot.getDiscrepancy())")
    InventorySnapshotResponseDto toDto(InventorySnapshot snapshot);

    InventorySnapshot toEntity(InventorySnapshotResponseDto dto);
}