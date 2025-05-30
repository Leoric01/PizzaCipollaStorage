package leoric.pizzacipollastorage.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VatRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private float rate;

    @OneToMany(mappedBy = "vatRate")
    private List<Ingredient> ingredients;
}