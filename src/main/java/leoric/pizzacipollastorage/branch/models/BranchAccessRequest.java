package leoric.pizzacipollastorage.branch.models;

import jakarta.persistence.*;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.branch.models.constants.BranchAccessRequestStatus;
import leoric.pizzacipollastorage.loans.models.Branch;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchAccessRequest {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Branch branch;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BranchAccessRequestStatus branchAccessRequestStatus;

    private LocalDateTime requestDate;
    private LocalDateTime approvalDate;

    @ManyToOne
    private User approvedBy;
}