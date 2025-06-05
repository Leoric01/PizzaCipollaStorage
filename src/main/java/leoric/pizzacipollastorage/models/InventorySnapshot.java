package leoric.pizzacipollastorage.models;

import jakarta.persistence.*;
import leoric.pizzacipollastorage.models.enums.SnapshotType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventorySnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private LocalDateTime timestamp;

    private float measuredQuantity;
    private Float expectedQuantity;

    private String note;

    @Enumerated(EnumType.STRING)
    private SnapshotType type;

    @Transient
    public Float getDiscrepancy() {
        if (type != SnapshotType.INVENTORY || expectedQuantity == null) return null;
        return measuredQuantity - expectedQuantity;
    }
}