package leoric.pizzacipollastorage.services.interfaces;

import leoric.pizzacipollastorage.DTOs.VatRateCreateDto;
import leoric.pizzacipollastorage.models.VatRate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VatRateService {
    VatRate createVatRate(VatRateCreateDto dto);

    List<VatRate> getAll();
}