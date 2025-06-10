package leoric.pizzacipollastorage.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
public class Packaging {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;         // "MenuItem krabice 32cm" nebo taska atp
    private String unit;         // "ks"
    private float lossFactor;

    private boolean trackStock;  // Odečítat ze skladu?
    private boolean billSeparately; // Účtovat samostatně zákazníkovi?

    private float priceWithoutVat;  // pokud účtujeme
}