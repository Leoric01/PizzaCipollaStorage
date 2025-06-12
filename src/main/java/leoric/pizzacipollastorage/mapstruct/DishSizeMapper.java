package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.DishSize.DishSizeCreateDto;
import leoric.pizzacipollastorage.DTOs.DishSize.DishSizeResponseDto;
import leoric.pizzacipollastorage.models.DishSize;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DishSizeMapper {
    DishSize toEntity(DishSizeCreateDto dto);
    DishSizeResponseDto toDto(DishSize entity);
    List<DishSizeResponseDto> toDtoList(List<DishSize> sizes);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget DishSize entity, DishSizeCreateDto dto);
}