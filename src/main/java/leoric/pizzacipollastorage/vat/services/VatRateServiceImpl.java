package leoric.pizzacipollastorage.vat.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.handler.exceptions.DuplicateVatRateNameException;
import leoric.pizzacipollastorage.vat.VatRateMapper;
import leoric.pizzacipollastorage.vat.dtos.VatRateCreateDto;
import leoric.pizzacipollastorage.vat.dtos.VatRateDeleteResponseDto;
import leoric.pizzacipollastorage.vat.dtos.VatRateShortDto;
import leoric.pizzacipollastorage.vat.models.VatRate;
import leoric.pizzacipollastorage.vat.repositories.VatRateRepository;
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

        VatRateDeleteResponseDto dto = new VatRateDeleteResponseDto();
        dto.setId(vatRate.getId());
        dto.setName(vatRate.getName());
        dto.setRate(vatRate.getRate());

        vatRateRepository.delete(vatRate);

        return dto;
    }

    @Override
    public VatRateShortDto getVatRateById(UUID id) {
        VatRate vatRate = vatRateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vat rate with ID " + id + " not found"));
        return vatRateMapper.toShortDto(vatRate);
    }
}