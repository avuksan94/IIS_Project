package av.iisproject.restapi.DTO;

import jakarta.persistence.Column;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class DTOImageResponse {
    private Long imageId;
    private String content;
}
