package leoric.pizzacipollastorage.auth.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Integer id;
    private String email;
    private String fullname;
    private List<String> role;
}