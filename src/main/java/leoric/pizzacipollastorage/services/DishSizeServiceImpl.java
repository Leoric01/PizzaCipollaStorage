package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.DishSize.DishSizeCreateDto;
import leoric.pizzacipollastorage.DTOs.DishSize.DishSizeResponseDto;
import leoric.pizzacipollastorage.mapstruct.DishSizeMapper;
import leoric.pizzacipollastorage.models.DishSize;
import leoric.pizzacipollastorage.repositories.DishSizeRepository;
import leoric.pizzacipollastorage.services.interfaces.DishSizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    @Override
    public DishSizeResponseDto update(UUID id, DishSizeCreateDto dto) {
        DishSize dishSize = dishSizeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DishSize with id " + id + " not found"));

        dishSize.setName(dto.getName());
        dishSize.setFactor(dto.getFactor());
        DishSize saved = dishSizeRepository.save(dishSize);
        return dishSizeMapper.toDto(saved);
    }

    @Override
    public void delete(UUID id) {
        if (!dishSizeRepository.existsById(id)) {
            throw new EntityNotFoundException("DishSize with id " + id + " not found");
        }
        dishSizeRepository.deleteById(id);
    }

}