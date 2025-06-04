package leoric.pizzacipollastorage.DTOs.PizzaSale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PizzaSaleResponseDto {
    private Long id;
    private String pizzaName;
    private String dishSize;
    private int quantitySold;
    private String cookName;
    private LocalDateTime saleDate;
}