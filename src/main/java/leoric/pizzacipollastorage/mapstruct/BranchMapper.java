package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.Loans.BranchCreateDto;
import leoric.pizzacipollastorage.DTOs.Loans.BranchResponseDto;
import leoric.pizzacipollastorage.models.Branch;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BranchMapper {
    Branch toEntity(BranchCreateDto dto);
    BranchResponseDto toDto(Branch branch);
    List<BranchResponseDto> toDtoList(List<Branch> branches);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Branch entity, BranchCreateDto dto);
}