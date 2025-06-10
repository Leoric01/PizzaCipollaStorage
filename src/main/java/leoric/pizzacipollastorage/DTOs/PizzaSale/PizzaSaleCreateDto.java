package leoric.pizzacipollastorage.DTOs.PizzaSale;

import lombok.Data;

import java.util.UUID;

@Data
public class PizzaSaleCreateDto {
    private UUID pizzaId;
    private UUID dishSizeId;
    private int quantitySold;
    private String cookName;
}