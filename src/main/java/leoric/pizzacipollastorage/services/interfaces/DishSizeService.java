package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.DishSize.DishSizeCreateDto;
import leoric.pizzacipollastorage.DTOs.DishSize.DishSizeResponseDto;
import leoric.pizzacipollastorage.auth.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface DishSizeService {
    DishSizeResponseDto create(DishSizeCreateDto dto, User connectedUser);
    List<DishSizeResponseDto> getAll();

    DishSizeResponseDto update(UUID id, DishSizeCreateDto dto);

    void delete(UUID id);
}