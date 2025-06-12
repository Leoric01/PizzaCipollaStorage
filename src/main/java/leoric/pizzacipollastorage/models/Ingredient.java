package leoric.pizzacipollastorage.models;

import jakarta.persistence.*;
import leoric.pizzacipollastorage.models.enums.ProductCategory;
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
public class Ingredient {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;
    private String unit;
    private float lossCleaningFactor;
    private float lossUsageFactor;

    @ManyToOne
    @JoinColumn(name = "vat_rate_id")
    private VatRate vatRate;

    @OneToMany(mappedBy = "ingredient")
    private List<IngredientPrice> prices;

    @OneToMany(mappedBy = "ingredient")
    private List<StockEntry> stockEntries;

    @OneToMany(mappedBy = "ingredient")
    private List<InventorySnapshot> inventorySnapshots;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IngredientAlias> aliases;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;
}