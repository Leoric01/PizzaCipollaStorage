package leoric.pizzacipollastorage.DTOs.PizzaSale;

import lombok.Data;

@Data
public class PizzaSaleCreateDto {
    private Long pizzaId;
    private Long dishSizeId;
    private int quantitySold;
    private String cookName;
}