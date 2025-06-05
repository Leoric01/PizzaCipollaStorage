package leoric.pizzacipollastorage.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate issuedDate;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate receivedDate;
    private String note;

    @OneToMany(mappedBy = "purchaseInvoice")
    private List<PurchaseInvoiceItem> items;
}