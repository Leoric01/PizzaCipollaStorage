package leoric.pizzacipollastorage.vat.services;

import leoric.pizzacipollastorage.vat.dtos.VatRateCreateDto;
import leoric.pizzacipollastorage.vat.dtos.VatRateDeleteResponseDto;
import leoric.pizzacipollastorage.vat.dtos.VatRateShortDto;
import leoric.pizzacipollastorage.vat.models.VatRate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface VatRateService {
    VatRate createVatRate(VatRateCreateDto dto);

    List<VatRateShortDto> getAll();

    VatRateDeleteResponseDto deleteVatRateById(UUID id);
}