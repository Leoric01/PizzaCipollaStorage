package leoric.pizzacipollastorage.models;

import jakarta.persistence.*;
import leoric.pizzacipollastorage.branch.models.Branch;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
public class Packaging {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;
    private String unit;
    private float lossFactor;

    private boolean trackStock;  // Odečítat ze skladu?
    private boolean billSeparately; // Účtovat samostatně zákazníkovi?

    private float priceWithoutVat;  // pokud účtujeme

    @ManyToOne(optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

}