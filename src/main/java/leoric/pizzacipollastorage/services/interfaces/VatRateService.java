package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.VatRateCreateDto;
import leoric.pizzacipollastorage.DTOs.VatRateDeleteResponseDto;
import leoric.pizzacipollastorage.DTOs.VatRateShortDto;
import leoric.pizzacipollastorage.models.VatRate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VatRateService {
    VatRate createVatRate(VatRateCreateDto dto);

    List<VatRateShortDto> getAll();

    VatRateDeleteResponseDto deleteVatRateById(Long id);
}