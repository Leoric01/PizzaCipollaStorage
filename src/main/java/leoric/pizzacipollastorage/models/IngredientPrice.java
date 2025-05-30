package leoric.pizzacipollastorage.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    private float pricePerUnitWithoutTax;
    private LocalDateTime receivedDate;
}