package av.task07.interoperability.DAL.Entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class JwtResponse {
    private String token;
}
