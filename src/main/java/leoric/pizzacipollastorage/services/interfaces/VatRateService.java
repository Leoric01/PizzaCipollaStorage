package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.Vat.VatRateCreateDto;
import leoric.pizzacipollastorage.DTOs.Vat.VatRateDeleteResponseDto;
import leoric.pizzacipollastorage.DTOs.Vat.VatRateShortDto;
import leoric.pizzacipollastorage.models.VatRate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface VatRateService {
    VatRate createVatRate(VatRateCreateDto dto);

    List<VatRateShortDto> getAll();

    VatRateDeleteResponseDto deleteVatRateById(UUID id);
}