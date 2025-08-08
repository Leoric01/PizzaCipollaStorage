package leoric.pizzacipollastorage.auth.models;

import jakarta.persistence.*;
import leoric.pizzacipollastorage.branch.models.Branch;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_branch_roles")
@EntityListeners(AuditingEntityListener.class)
public class UserBranchRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Branch branch;

    @ManyToOne(optional = false)
    private Role role;
}