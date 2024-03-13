package av.task07.interoperability.VM;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class ImageViewModel {
    private Long imageId;
    @NotBlank(message = "The image URL cannot be left blank")
    @Size(min = 5, max = 255, message = "The image URL must be between 5 and 255 characters long")
    @URL(protocol = "https", message = "Invalid image URL format,please check the link!")
    private String content;

    public ImageViewModel(String content) {
        this.content = content;
    }
}

