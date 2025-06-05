package leoric.pizzacipollastorage.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
}