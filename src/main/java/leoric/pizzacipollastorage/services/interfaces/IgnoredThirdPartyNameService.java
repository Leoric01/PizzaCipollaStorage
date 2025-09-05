package leoric.pizzacipollastorage.services.interfaces;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface IgnoredThirdPartyNameService {
    List<String> listIgnoredNames(UUID branchId);

    void removeIgnoredName(UUID branchId, String name);

    void addIgnoredName(UUID branchId, String name);

    void addIgnoredNameBulk(UUID branchId, List<String> names);
}