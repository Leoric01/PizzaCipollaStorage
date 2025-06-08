package leoric.pizzacipollastorage.init;

import jakarta.annotation.PostConstruct;
import leoric.pizzacipollastorage.models.Supplier;
import leoric.pizzacipollastorage.repositories.SupplierRepository;
import leoric.pizzacipollastorage.utils.CustomUtilityString;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SupplierInitializer {

    private final SupplierRepository supplierRepository;

    @PostConstruct
    public void insertDefaultSuppliers() {
        createIfNotExists("Nowaco s.r.o.", "kontakt@nowaco.cz");
        createIfNotExists("Cortelazzi s.r.o.", "kontakt@cortellazi.cz");
        createIfNotExists("Zelenina s.r.o.", "info@zelenina.cz");
        createIfNotExists("Makro", "info@makro.cz");
    }

    private void createIfNotExists(String name, String contactInfo) {
        String normalized = CustomUtilityString.normalize(name);

        boolean exists = supplierRepository.findAll().stream()
                .map(Supplier::getName)
                .map(CustomUtilityString::normalize)
                .anyMatch(n -> n.equals(normalized));

        if (!exists) {
            Supplier supplier = Supplier.builder()
                    .name(name)
                    .contactInfo(contactInfo)
                    .build();
            supplierRepository.save(supplier);
        }
    }
}