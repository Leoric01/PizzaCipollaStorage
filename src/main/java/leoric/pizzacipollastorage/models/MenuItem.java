package leoric.pizzacipollastorage.models;

import jakarta.persistence.*;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.models.enums.DishSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItem {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private MenuItemCategory category;

    @ManyToOne(optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DishSize dishSize;

    @ElementCollection
    @CollectionTable(
            name = "menu_item_third_party_names",
            joinColumns = @JoinColumn(name = "menu_item_id")
    )
    @Column(name = "third_party_name")
    private List<String> thirdPartyNames = new ArrayList<>();

    @Override
    public String toString() {
        return "MenuItem{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", category=" + (category != null ? category.getName() : null) +
               ", branchId=" + (branch != null ? branch.getId() : null) +
               ", dishSize=" + dishSize +
               ", thirdPartyNames=" + thirdPartyNames +
               ", recipeIngredientsCount=" + (recipeIngredients != null ? recipeIngredients.size() : 0) +
               '}';
    }

}