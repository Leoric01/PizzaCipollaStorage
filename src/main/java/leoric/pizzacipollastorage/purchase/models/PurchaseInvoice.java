package leoric.pizzacipollastorage.purchase.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseInvoice {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

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