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
public class MenuItemThirdPartyNameUpdateDto {
    @NotBlank(message = "Old name must not be blank")
    private String oldName;

    @NotBlank(message = "New name must not be blank")
    private String newName;
}