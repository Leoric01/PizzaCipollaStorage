package leoric.pizzacipollastorage.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PizzaSaleExtra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "pizza_sale_id")
    private PizzaSale pizzaSale;

    @ManyToOne
    @JoinColumn(name = "extra_id")
    private Extra extra;

    private int quantity;
}