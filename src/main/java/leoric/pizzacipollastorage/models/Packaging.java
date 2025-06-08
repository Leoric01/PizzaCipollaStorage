package leoric.pizzacipollastorage.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Packaging {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;         // "Pizza krabice 32cm" nebo taska atp
    private String unit;         // "ks"
    private float lossFactor;

    private boolean trackStock;  // Odečítat ze skladu?
    private boolean billSeparately; // Účtovat samostatně zákazníkovi?

    private float priceWithoutVat;  // pokud účtujeme
}