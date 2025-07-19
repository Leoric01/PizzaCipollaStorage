package leoric.pizzacipollastorage.vat.dtos;

import lombok.Data;

@Data
public class VatRateCreateDto {
    private String name;
    private float rate;
}