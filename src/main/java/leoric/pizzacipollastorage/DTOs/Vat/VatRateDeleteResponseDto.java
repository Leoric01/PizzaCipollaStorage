package leoric.pizzacipollastorage.DTOs.Vat;

import lombok.Data;

@Data
public class VatRateDeleteResponseDto {
    private Long id;
    private String name;
    private float rate;
}