package leoric.pizzacipollastorage.loans.models;

import jakarta.persistence.*;
import leoric.pizzacipollastorage.models.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientLoanItem {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ingredient_loan_id")
    private IngredientLoan ingredientLoan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private Float quantity;
    private String note;

    public String toSimpleString() {
        return "IngredientLoanItem{" +
                "id=" + id +
                ", ingredientId=" + (ingredient != null ? ingredient.getId() : null) +
                ", ingredientName=" + (ingredient != null ? ingredient.getName() : null) +
                ", quantity=" + quantity +
                ", note='" + note + '\'' +
                '}';
    }
}