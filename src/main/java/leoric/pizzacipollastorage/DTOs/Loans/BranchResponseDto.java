package leoric.pizzacipollastorage.DTOs.Loans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchResponseDto {
    private Long id;
    private String name;
    private String address;
    private String contactInfo;
}