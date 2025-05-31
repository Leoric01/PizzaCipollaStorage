package leoric.pizzacipollastorage.DTOs;

import lombok.Data;

@Data
public class VatRateDeleteResponseDto {
    private Long id;
    private String name;
    private float rate;
}