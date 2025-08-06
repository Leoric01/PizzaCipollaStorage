package leoric.pizzacipollastorage.mapstruct;

import leoric.pizzacipollastorage.branch.dtos.BranchAccessRequestResponseDto;
import leoric.pizzacipollastorage.branch.models.BranchAccessRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BranchAccessRequestMapper {

    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullname", target = "fullname")
    @Mapping(source = "approvedBy.id", target = "approvedBy")
    @Mapping(source = "branchAccessRequestStatus", target = "status")
    BranchAccessRequestResponseDto toDto(BranchAccessRequest request);

    List<BranchAccessRequestResponseDto> toDtoList(List<BranchAccessRequest> requests);
}