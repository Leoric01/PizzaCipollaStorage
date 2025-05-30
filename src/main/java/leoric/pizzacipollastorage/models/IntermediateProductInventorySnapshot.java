package leoric.pizzacipollastorage.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntermediateProductInventorySnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "intermediate_product_id")
    private IntermediateProduct intermediateProduct;

    private LocalDateTime timestamp;
    private float measuredQuantity;

}