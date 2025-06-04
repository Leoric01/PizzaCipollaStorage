package leoric.pizzacipollastorage.services;

import leoric.pizzacipollastorage.DTOs.DishSize.DishSizeCreateDto;
import leoric.pizzacipollastorage.DTOs.DishSize.DishSizeResponseDto;
import leoric.pizzacipollastorage.mapstruct.DishSizeMapper;
import leoric.pizzacipollastorage.models.DishSize;
import leoric.pizzacipollastorage.repositories.DishSizeRepository;
import leoric.pizzacipollastorage.services.interfaces.DishSizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishSizeServiceImpl implements DishSizeService {

    private final DishSizeRepository dishSizeRepository;
    private final DishSizeMapper dishSizeMapper;

    @Override
    public DishSizeResponseDto create(DishSizeCreateDto dto) {
        DishSize saved = dishSizeRepository.save(dishSizeMapper.toEntity(dto));
        return dishSizeMapper.toDto(saved);
    }

    @Override
    public List<DishSizeResponseDto> getAll() {
        return dishSizeMapper.toDtoList(dishSizeRepository.findAll());
    }
}