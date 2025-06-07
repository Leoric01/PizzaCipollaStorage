package leoric.pizzacipollastorage.models;

import jakarta.persistence.*;
import leoric.pizzacipollastorage.models.enums.LoanStatus;
import leoric.pizzacipollastorage.models.enums.LoanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    @ManyToOne
    @JoinColumn(name = "from_branch_id")
    private Branch fromBranch;

    @ManyToOne
    @JoinColumn(name = "to_branch_id")
    private Branch toBranch;

    private LocalDate createdAt;

    private String note;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @OneToMany(mappedBy = "ingredientLoan", cascade = CascadeType.ALL)
    private List<IngredientLoanItem> items;
}