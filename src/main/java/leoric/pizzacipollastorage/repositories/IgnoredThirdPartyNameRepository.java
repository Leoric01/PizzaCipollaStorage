package leoric.pizzacipollastorage.repositories;

import leoric.pizzacipollastorage.models.IgnoredThirdPartyName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IgnoredThirdPartyNameRepository extends JpaRepository<IgnoredThirdPartyName, UUID> {
    List<IgnoredThirdPartyName> findByBranchId(UUID branchId);

    void deleteByBranchIdAndName(UUID branchId, String name);

    boolean existsByBranchIdAndName(UUID branchId, String name);

    List<IgnoredThirdPartyName> findByBranchIdAndNameIn(UUID branchId, List<String> names);

    Optional<IgnoredThirdPartyName> findByBranchIdAndName(UUID branchId, String thirdPartyName);
}