package leoric.pizzacipollastorage.loans.models;

import jakarta.persistence.*;
import leoric.pizzacipollastorage.auth.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

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

    @OneToMany(mappedBy = "fromBranch")
    private List<IngredientLoan> loansGiven;

    @OneToMany(mappedBy = "toBranch")
    private List<IngredientLoan> loansReceived;

    @ManyToMany(mappedBy = "branches")
    private List<User> users;
}