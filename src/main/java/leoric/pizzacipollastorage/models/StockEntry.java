package leoric.pizzacipollastorage.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import leoric.pizzacipollastorage.purchase.models.PurchaseInvoiceItem;
import leoric.pizzacipollastorage.purchase.models.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockEntry {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "purchase_invoice_item_id")
    private PurchaseInvoiceItem purchaseInvoiceItem;

    private float quantityReceived;
    private float pricePerUnitWithoutTax;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate receivedDate;
}