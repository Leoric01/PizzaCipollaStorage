package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.PizzaSale.PizzaSaleCreateDto;
import leoric.pizzacipollastorage.DTOs.PizzaSale.PizzaSaleResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface PizzaSaleService {
    PizzaSaleResponseDto createSale(PizzaSaleCreateDto dto);
}