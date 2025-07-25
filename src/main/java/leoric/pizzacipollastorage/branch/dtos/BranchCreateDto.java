package leoric.pizzacipollastorage.branch.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchCreateDto {
    private String name;
    private String address;
    private String contactInfo;
}