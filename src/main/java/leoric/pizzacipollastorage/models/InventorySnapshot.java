package leoric.pizzacipollastorage.models;

import jakarta.persistence.*;
import leoric.pizzacipollastorage.models.enums.IngredientForm;
import leoric.pizzacipollastorage.models.enums.SnapshotType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventorySnapshot {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private LocalDateTime timestamp;

    private float measuredQuantity;
    private Float expectedQuantity;

    private String note;

    @Enumerated(EnumType.STRING)
    private SnapshotType type;

    @Enumerated(EnumType.STRING)
    private IngredientForm form;

    @Transient
    public Float getDiscrepancy() {
        if (type != SnapshotType.INVENTORY || expectedQuantity == null) return null;
        return measuredQuantity - expectedQuantity;
    }
}