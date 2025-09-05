package leoric.pizzacipollastorage.inventory.models;

import jakarta.persistence.*;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.enums.IngredientState;
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

    private Float measuredQuantity;
    private Float expectedQuantity;
    private Float lastDiscrepancy;

    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    private Branch branch;

    @Enumerated(EnumType.STRING)
    private SnapshotType type;

    @Enumerated(EnumType.STRING)
    private IngredientState form;

    @Transient
    public Float getDiscrepancy() {
        if (type != SnapshotType.INVENTORY || expectedQuantity == null) return null;
        return measuredQuantity - expectedQuantity;
    }

    @Override
    public String toString() {
        return "InventorySnapshot{" +
               "id=" + id +
               ", ingredient=" + (ingredient != null ? ingredient.getName() : null) +
               ", timestamp=" + timestamp +
               ", measuredQuantity=" + measuredQuantity +
               ", expectedQuantity=" + expectedQuantity +
               ", discrepancy=" + getDiscrepancy() +
               ", lastDiscrepancy=" + lastDiscrepancy +
               ", branchId=" + (branch != null ? branch.getId() : null) +
               ", type=" + type +
               ", form=" + form +
               ", note='" + note + '\'' +
               '}';
    }
}