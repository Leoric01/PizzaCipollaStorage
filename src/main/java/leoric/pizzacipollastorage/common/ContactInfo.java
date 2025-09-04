package leoric.pizzacipollastorage.common;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactInfo {
    private String contactEmail;
    private String phoneNumber;
}