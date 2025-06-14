package leoric.pizzacipollastorage.vat.models;

import jakarta.persistence.*;
import leoric.pizzacipollastorage.models.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategory {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vat_rate_id", nullable = false)
    private VatRate vatRate;

    @OneToMany(mappedBy = "productCategory")
    private List<Ingredient> ingredients;
}