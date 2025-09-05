package leoric.pizzacipollastorage.DTOs.MenuItemSale;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IgnoredThirdPartyNameBulkCreateDto {
    @NotEmpty(message = "List of names must not be empty")
    private List<@NotBlank(message = "Name must not be blank") String> names;
}