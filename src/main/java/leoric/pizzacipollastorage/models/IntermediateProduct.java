package leoric.pizzacipollastorage.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntermediateProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String unit;

    @OneToMany(mappedBy = "intermediateProduct")
    private List<IntermediateProductIngredient> ingredients;

}