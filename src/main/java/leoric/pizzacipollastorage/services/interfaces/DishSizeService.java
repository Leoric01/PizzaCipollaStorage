package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.DishSize.DishSizeCreateDto;
import leoric.pizzacipollastorage.DTOs.DishSize.DishSizeResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DishSizeService {
    DishSizeResponseDto create(DishSizeCreateDto dto);
    List<DishSizeResponseDto> getAll();
}