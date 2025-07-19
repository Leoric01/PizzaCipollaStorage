package leoric.pizzacipollastorage.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeIngredient {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    /**
     * Množství v jednotce ingredience – buď absolutní, nebo základní, dle autoScale.
     */
    private Float quantity;

    /**
     * Má se množství automaticky vynásobit `dishSize.defaultFactor`?
     * true = quantity je pro základní velikost, přepočítá se
     * false = quantity je pro konkrétní velikost (ručně)
     */
    @Builder.Default
    private boolean autoScaleWithDishSize = true;
}