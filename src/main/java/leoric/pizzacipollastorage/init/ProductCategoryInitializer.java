package leoric.pizzacipollastorage.init;

import jakarta.annotation.PostConstruct;
import leoric.pizzacipollastorage.vat.repositories.ProductCategoryRepository;
import leoric.pizzacipollastorage.vat.repositories.VatRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("productCategoryInitializer")
@RequiredArgsConstructor
@DependsOn({"vatRateInitializer"})
public class ProductCategoryInitializer {

    private final ProductCategoryRepository productCategoryRepository;
    private final VatRateRepository vatRateRepository;

    @PostConstruct
    public void insertDefaultCategories() {
        insert("FOOD", UUID.fromString("00000000-0000-0000-0000-000000000002"));
        insert("DRINK", UUID.fromString("00000000-0000-0000-0000-000000000001"));
        insert("PACKAGING", UUID.fromString("00000000-0000-0000-0000-000000000001"));
    }

    private void insert(String name, UUID vatRateId) {
//        if (productCategoryRepository.findByNameIgnoreCase(name).isEmpty()) {
//            VatRate vatRate = vatRateRepository.findById(vatRateId)
//                    .orElseThrow(() -> new IllegalStateException("Missing default VAT rate for category"));
//
//            ProductCategory category = ProductCategory.builder()
//                    .name(name)
//                    .vatRate(vatRate)
//                    .build();
//
//            productCategoryRepository.save(category);
//        }
    }
}