package leoric.pizzacipollastorage.models;

import jakarta.persistence.*;
import leoric.pizzacipollastorage.models.enums.MenuItemCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItem {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private MenuItemCategory category;

    @OneToMany(mappedBy = "menuItem")
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();
}