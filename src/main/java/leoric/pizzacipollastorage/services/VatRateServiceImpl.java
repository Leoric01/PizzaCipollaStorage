package leoric.pizzacipollastorage.services;

import leoric.pizzacipollastorage.DTOs.VatRateCreateDto;
import leoric.pizzacipollastorage.models.VatRate;
import leoric.pizzacipollastorage.repositories.VatRateRepository;
import leoric.pizzacipollastorage.services.interfaces.VatRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VatRateServiceImpl implements VatRateService {

    private final VatRateRepository vatRateRepository;

    @Override
    public VatRate createVatRate(VatRateCreateDto dto) {
        VatRate vatRate = new VatRate();
        vatRate.setName(dto.getName());
        vatRate.setRate(dto.getRate());
        return vatRateRepository.save(vatRate);
    }

    @Override
    public List<VatRate> getAll() {
        return vatRateRepository.findAll();
    }
}