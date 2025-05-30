package leoric.pizzacipollastorage.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceNumber;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    private LocalDateTime issuedDate;
    private LocalDateTime receivedDate;
    private String note;

    @OneToMany(mappedBy = "purchaseInvoice")
    private List<PurchaseInvoiceItem> items;
}