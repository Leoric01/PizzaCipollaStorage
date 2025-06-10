package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.DTOs.Vat.VatRateCreateDto;
import leoric.pizzacipollastorage.DTOs.Vat.VatRateDeleteResponseDto;
import leoric.pizzacipollastorage.DTOs.Vat.VatRateShortDto;
import leoric.pizzacipollastorage.handler.exceptions.DuplicateVatRateNameException;
import leoric.pizzacipollastorage.mapstruct.VatRateMapper;
import leoric.pizzacipollastorage.models.VatRate;
import leoric.pizzacipollastorage.repositories.VatRateRepository;
import leoric.pizzacipollastorage.services.interfaces.VatRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VatRateServiceImpl implements VatRateService {

    private final VatRateRepository vatRateRepository;
    private final VatRateMapper vatRateMapper;

    @Override
    public VatRate createVatRate(VatRateCreateDto dto) {
        boolean exists = vatRateRepository.existsByName(dto.getName());
        if (exists) {
            throw new DuplicateVatRateNameException("VAT rate with name '" + dto.getName() + "' already exists");
        }

        VatRate vatRate = new VatRate();
        vatRate.setName(dto.getName());
        vatRate.setRate(dto.getRate());
        return vatRateRepository.save(vatRate);
    }

    @Override
    public List<VatRateShortDto> getAll() {
        List<VatRate> vatRates = vatRateRepository.findAll();
        return vatRateMapper.toShortDtoList(vatRates);
    }

    @Override
    public VatRateDeleteResponseDto deleteVatRateById(UUID id) {
        VatRate vatRate = vatRateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vat rate with ID " + id + " not found"));

        vatRateRepository.delete(vatRate);

        VatRateDeleteResponseDto dto = new VatRateDeleteResponseDto();
        dto.setId(vatRate.getId());
        dto.setName(vatRate.getName());
        dto.setRate(vatRate.getRate());
        return dto;
    }
}