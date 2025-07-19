package leoric.pizzacipollastorage.branch.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchResponseDto {
    private UUID id;
    private String name;
    private String address;
    private String contactInfo;
}