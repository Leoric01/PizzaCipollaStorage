package leoric.pizzacipollastorage.DTOs.Vat;

import lombok.Data;

@Data
public class VatRateCreateDto {
    private String name;
    private float rate;
}