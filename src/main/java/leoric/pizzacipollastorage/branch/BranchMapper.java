package leoric.pizzacipollastorage.branch;

import leoric.pizzacipollastorage.branch.dtos.BranchCreateDto;
import leoric.pizzacipollastorage.branch.dtos.BranchResponseDto;
import leoric.pizzacipollastorage.branch.models.Branch;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface BranchMapper {
    Branch toEntity(BranchCreateDto dto);
    BranchResponseDto toDto(Branch branch);
    List<BranchResponseDto> toDtoList(List<Branch> branches);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Branch entity, BranchCreateDto dto);

    default UUID map(Branch branch) {
        return branch.getId();
    }
}