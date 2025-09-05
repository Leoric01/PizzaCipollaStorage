package leoric.pizzacipollastorage.services;

import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.branch.repositories.BranchRepository;
import leoric.pizzacipollastorage.models.IgnoredThirdPartyName;
import leoric.pizzacipollastorage.repositories.IgnoredThirdPartyNameRepository;
import leoric.pizzacipollastorage.services.interfaces.IgnoredThirdPartyNameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IgnoredThirdPartyNameServiceImpl implements IgnoredThirdPartyNameService {

    private final IgnoredThirdPartyNameRepository ignoredThirdPartyNameRepository;
    private final BranchRepository branchRepository;

    @Override
    @Transactional
    public List<String> listIgnoredNames(UUID branchId) {
        return ignoredThirdPartyNameRepository.findByBranchId(branchId).stream()
                .map(IgnoredThirdPartyName::getName)
                .toList();
    }

    @Override
    @Transactional
    public void removeIgnoredName(UUID branchId, String name) {
        ignoredThirdPartyNameRepository.deleteByBranchIdAndName(branchId, name);
    }

    @Override
    @Transactional
    public void addIgnoredName(UUID branchId, String name) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        if (!ignoredThirdPartyNameRepository.existsByBranchIdAndName(branchId, name)) {
            ignoredThirdPartyNameRepository.save(IgnoredThirdPartyName.builder()
                    .branch(branch)
                    .name(name)
                    .build());
        }
    }

    @Override
    @Transactional
    public void addIgnoredNameBulk(UUID branchId, List<String> names) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        // Zjistíme, která jména už v DB existují
        List<String> existingNames = ignoredThirdPartyNameRepository
                .findByBranchIdAndNameIn(branchId, names)
                .stream()
                .map(IgnoredThirdPartyName::getName)
                .toList();

        // Připravíme entity, které ještě neexistují
        List<IgnoredThirdPartyName> toSave = names.stream()
                .filter(name -> !existingNames.contains(name))
                .map(name -> IgnoredThirdPartyName.builder()
                        .branch(branch)
                        .name(name)
                        .build())
                .toList();

        // Uložíme všechny najednou
        ignoredThirdPartyNameRepository.saveAll(toSave);
    }
}