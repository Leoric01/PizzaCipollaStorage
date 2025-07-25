package leoric.pizzacipollastorage.purchase.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import leoric.pizzacipollastorage.models.IngredientPrice;
import leoric.pizzacipollastorage.models.StockEntry;
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
public class Supplier {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;
    private String contactInfo;

    @OneToMany(mappedBy = "supplier")
    private List<StockEntry> stockEntries;

    @OneToMany(mappedBy = "supplier")
    private List<IngredientPrice> prices;
}