package leoric.pizzacipollastorage.DTOs;

import lombok.Data;

@Data
public class VatRateCreateDto {
    private String name;
    private float rate;
}