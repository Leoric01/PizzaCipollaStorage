package leoric.pizzacipollastorage.init;

import jakarta.annotation.PostConstruct;
import leoric.pizzacipollastorage.loans.models.Branch;
import leoric.pizzacipollastorage.loans.repositories.BranchRepository;
import leoric.pizzacipollastorage.utils.CustomUtilityString;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BranchInitializer {

    private final BranchRepository branchRepository;

    @PostConstruct
    public void insertDefaultBranches() {
        createIfNotExists("PPC Kamýk", "Freiwaldova 627/1, Praha", "+420 702 201 020");
        createIfNotExists("PPC Háje", "Brandlova 2381/1b, Praha", "+420 721 555 050");
        createIfNotExists("PPC Smíchov", "U Nikolajky 1097/3, Praha", "+420 720 696 999");
    }

    private void createIfNotExists(String name, String address, String contact) {
        String normalizedInput = CustomUtilityString.normalize(name);

        boolean exists = branchRepository.findAll().stream()
                .map(Branch::getName)
                .map(CustomUtilityString::normalize)
                .anyMatch(existing -> existing.equals(normalizedInput));

        if (!exists) {
            Branch branch = Branch.builder()
                    .name(name)
                    .address(address)
                    .contactInfo(contact)
                    .build();
            branchRepository.save(branch);
        }
    }
}