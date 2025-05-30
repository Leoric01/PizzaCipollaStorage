package leoric.pizzacipollastorage.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntermediateProductIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "intermediate_product_id")
    private IntermediateProduct intermediateProduct;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private float quantity;
}