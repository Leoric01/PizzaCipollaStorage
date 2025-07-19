package leoric.pizzacipollastorage.vat.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VatRate {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;
    private float rate;

    @OneToMany(mappedBy = "vatRate")
    private List<ProductCategory> categories;
}