package leoric.pizzacipollastorage.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String contactInfo;

    @OneToMany(mappedBy = "supplier")
    private List<StockEntry> stockEntries;

    @OneToMany(mappedBy = "supplier")
    private List<IngredientPrice> prices;
}