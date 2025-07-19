package leoric.pizzacipollastorage.purchase.models;

import jakarta.persistence.*;
import leoric.pizzacipollastorage.models.Ingredient;
import leoric.pizzacipollastorage.models.enums.OrderItemStatus;
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
public class PurchaseOrderItem {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private float quantityOrdered;

    private float quantityReceived;

    @Enumerated(EnumType.STRING)
    private OrderItemStatus status;

    private String note;
}