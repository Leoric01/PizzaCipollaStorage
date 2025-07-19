package leoric.pizzacipollastorage.models;

import jakarta.persistence.*;
import leoric.pizzacipollastorage.models.enums.DishSize;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
public class PackagingRule {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DishSize dishSize;

    @ManyToOne
    private Packaging packaging;

    private boolean defaultForTakeaway;
}