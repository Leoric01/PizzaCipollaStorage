package leoric.pizzacipollastorage.models;

import jakarta.persistence.*;

@Entity
public class PackagingRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private DishSize dishSize;

    @ManyToOne
    private Packaging packaging;

    private boolean defaultForTakeaway;
}