package leoric.pizzacipollastorage.vat.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class VatRateDeleteResponseDto {
    private UUID id;
    private String name;
    private float rate;
}