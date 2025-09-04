package leoric.pizzacipollastorage.init.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierBootstrapDto {
    private String name;
    private String contactInfo;
}