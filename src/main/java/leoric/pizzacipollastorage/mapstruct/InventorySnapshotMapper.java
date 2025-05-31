package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.InventorySnapshotResponseDto;
import leoric.pizzacipollastorage.models.InventorySnapshot;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {IngredientMapper.class})
public interface InventorySnapshotMapper {
    InventorySnapshotResponseDto toDto(InventorySnapshot snapshot);
}