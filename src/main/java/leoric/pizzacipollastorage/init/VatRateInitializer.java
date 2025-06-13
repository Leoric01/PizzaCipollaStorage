package leoric.pizzacipollastorage.init;

import jakarta.annotation.PostConstruct;
import leoric.pizzacipollastorage.utils.CustomUtilityString;
import leoric.pizzacipollastorage.vat.models.VatRate;
import leoric.pizzacipollastorage.vat.repositories.VatRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("vatRateInitializer")
@RequiredArgsConstructor
public class VatRateInitializer {

    private final VatRateRepository vatRateRepository;

    @PostConstruct
    public void insertDefaultVatRates() {
        createIfNotExists(UUID.fromString("00000000-0000-0000-0000-000000000001"), "dph - základní", 0.21f);
        createIfNotExists(UUID.fromString("00000000-0000-0000-0000-000000000002"), "dph - snížená", 0.12f);
        createIfNotExists(UUID.fromString("00000000-0000-0000-0000-000000000003"), "dph - nulová", 0.0f);
    }

    private void createIfNotExists(UUID id, String name, float rate) {
        String normalizedInput = CustomUtilityString.normalize(name);

        boolean exists = vatRateRepository.findAll().stream()
                .map(VatRate::getName)
                .map(CustomUtilityString::normalize)
                .anyMatch(existing -> existing.equals(normalizedInput));

        if (!exists) {
            VatRate vatRate = VatRate.builder()
                    .id(id)
                    .name(name)
                    .rate(rate)
                    .build();
            vatRateRepository.save(vatRate);
        }
    }
}