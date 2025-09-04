package leoric.pizzacipollastorage.branch.models;

import jakarta.persistence.*;
import leoric.pizzacipollastorage.auth.models.User;
import leoric.pizzacipollastorage.auth.models.UserBranchRole;
import leoric.pizzacipollastorage.common.Address;
import leoric.pizzacipollastorage.common.ContactInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Branch {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    private String name;

    @Embedded
    private Address address;

    @Embedded
    private ContactInfo contactInfo;

    @ManyToMany(mappedBy = "branches", fetch = FetchType.EAGER)
    private List<User> users = new ArrayList<>();

    @ManyToOne(optional = false)
    private User createdByManager;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<UserBranchRole> userBranchRoles = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Branch)) return false;
        Branch branch = (Branch) o;
        return id != null && id.equals(branch.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}