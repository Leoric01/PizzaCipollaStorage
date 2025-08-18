package leoric.pizzacipollastorage.loans.models;

import jakarta.persistence.*;
import leoric.pizzacipollastorage.branch.models.Branch;
import leoric.pizzacipollastorage.models.enums.LoanStatus;
import leoric.pizzacipollastorage.models.enums.LoanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientLoan {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    private LocalDate createdAt;

    private LocalDate returnedAt;

    private String note;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    private String counterpartyName;

    private String counterpartyContact;

    @OneToMany(mappedBy = "ingredientLoan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IngredientLoanItem> items;
}