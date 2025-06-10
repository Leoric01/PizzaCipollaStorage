package leoric.pizzacipollastorage.DTOs.DishSize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DishSizeResponseDto {
    private UUID id;
    private String name;
    private float factor;
}