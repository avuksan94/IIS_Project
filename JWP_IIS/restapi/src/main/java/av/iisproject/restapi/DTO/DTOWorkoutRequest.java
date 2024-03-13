package av.iisproject.restapi.DTO;

import av.iisproject.restapi.CustomAnnotations.MustBeValidEnum;
import av.iisproject.restapi.DAL.Entity.Image;
import av.iisproject.restapi.DAL.Enum.WorkoutType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class DTOWorkoutRequest {
    @NotBlank(message = "The workout name cannot be blank")
    @Size(min = 5, max = 255, message = "The name must be between 5 and 255 characters long")
    private String name;

    @NotBlank(message = "The instructions cannot be blank")
    @Size(min = 5, max = 1000, message = "The image URL must be between 5 and 1000 characters long")
    private String instructions;

    //@MustBeValidEnum(enumClass = WorkoutType.class, message = "Invalid workout type. Allowed values are CHEST, LEGS, BACK, TRICEPS, BICEPS, NECK.")
    private WorkoutType type;

    @Min(value = 1, message = "Number of sets must be more than 0")
    @Max(value = 12, message = "Number of sets must be less than or equal to 12")
    private Integer numberOfSets;

    @Min(value = 1, message = "The duration of the set must be more than 0")
    @Max(value = 180, message = "The duration of the set must be less than or equal to 180 seconds")
    private Integer setDuration;

    @NotBlank(message = "The image URL cannot be left blank")
    @Size(min = 5, max = 255, message = "The image URL must be between 5 and 255 characters long")
    @URL(protocol = "https", message = "Invalid image URL format,please check the link!")
    private String content;
}
