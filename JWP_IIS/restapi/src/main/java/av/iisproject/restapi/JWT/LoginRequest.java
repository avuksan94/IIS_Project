package av.iisproject.restapi.JWT;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class LoginRequest {
    private String username;
    private String password;
}