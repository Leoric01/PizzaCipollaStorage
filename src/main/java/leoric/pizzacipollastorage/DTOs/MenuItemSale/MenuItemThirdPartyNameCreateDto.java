package leoric.pizzacipollastorage.DTOs.MenuItemSale;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuItemThirdPartyNameCreateDto {
    @NotBlank(message = "Name must not be blank")
    private String name;
}