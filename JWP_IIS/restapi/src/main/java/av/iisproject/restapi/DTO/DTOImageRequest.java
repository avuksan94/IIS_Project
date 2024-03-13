package av.iisproject.restapi.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class DTOImageRequest {
    @NotBlank
    @URL(protocol = "https", message = "Invalid URL format")
    private String content;


}
