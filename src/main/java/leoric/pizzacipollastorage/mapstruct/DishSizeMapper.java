package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.DishSize.DishSizeCreateDto;
import leoric.pizzacipollastorage.DTOs.DishSize.DishSizeResponseDto;
import leoric.pizzacipollastorage.models.DishSize;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DishSizeMapper {
    DishSize toEntity(DishSizeCreateDto dto);
    DishSizeResponseDto toDto(DishSize entity);
    List<DishSizeResponseDto> toDtoList(List<DishSize> sizes);
}