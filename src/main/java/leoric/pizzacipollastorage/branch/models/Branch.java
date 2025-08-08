package leoric.pizzacipollastorage.branch.models;

import jakarta.persistence.*;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.auth.models.UserBranchRole;
import leoric.pizzacipollastorage.loans.models.IngredientLoan;
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
public class Branch {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;
    private String address;
    private String contactInfo;

    @ManyToMany(mappedBy = "branches")
    private List<User> users = new ArrayList<>();

    @ManyToOne(optional = false)
    private User createdByManager;

    @OneToMany(mappedBy = "fromBranch")
    private List<IngredientLoan> loansGiven = new ArrayList<>();

    @OneToMany(mappedBy = "toBranch")
    private List<IngredientLoan> loansReceived = new ArrayList<>();

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBranchRole> userBranchRoles = new ArrayList<>();
}