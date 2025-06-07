package leoric.pizzacipollastorage.init;

import jakarta.annotation.PostConstruct;
import leoric.pizzacipollastorage.models.VatRate;
import leoric.pizzacipollastorage.repositories.VatRateRepository;
import leoric.pizzacipollastorage.utils.CustomUtilityString;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("vatRateInitializer")
@RequiredArgsConstructor
public class VatRateInitializer {

    private final VatRateRepository vatRateRepository;

    @PostConstruct
    public void insertDefaultVatRates() {
        createIfNotExists("dph - snížená", 0.12f);
        createIfNotExists("dph - základní", 0.21f);
        createIfNotExists("dph - nulová", 0.0f);
    }

    private void createIfNotExists(String name, float rate) {
        String normalizedInput = CustomUtilityString.normalize(name);

        boolean exists = vatRateRepository.findAll().stream()
                .map(VatRate::getName)
                .map(CustomUtilityString::normalize)
                .anyMatch(existing -> existing.equals(normalizedInput));

        if (!exists) {
            VatRate vatRate = VatRate.builder()
                    .name(name)
                    .rate(rate)
                    .build();
            vatRateRepository.save(vatRate);
        }
    }
}