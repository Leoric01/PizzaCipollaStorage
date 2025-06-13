package leoric.pizzacipollastorage.vat.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VatRateShortDto {
    private UUID id;
    private String name;
    private float rate;
}