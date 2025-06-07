package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.DTOs.Loans.BranchCreateDto;
import leoric.pizzacipollastorage.DTOs.Loans.BranchResponseDto;
import leoric.pizzacipollastorage.models.Branch;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BranchMapper {
    Branch toEntity(BranchCreateDto dto);
    BranchResponseDto toDto(Branch branch);
    List<BranchResponseDto> toDtoList(List<Branch> branches);
}