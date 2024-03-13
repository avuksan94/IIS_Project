package av.task07.interoperability.VM;

import av.task07.interoperability.DAL.Enum.WorkoutType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.internal.IgnoreForbiddenApisErrors;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class WorkoutViewModel {
    private Long workoutId;
    private String name;
    private String instructions;
    private WorkoutType type;
    private Integer numberOfSets;
    private Integer setDuration;
    private ImageViewModel image;

    public WorkoutViewModel(String name, String instructions, WorkoutType type, Integer numberOfSets, Integer setDuration, ImageViewModel image) {
        this.name = name;
        this.instructions = instructions;
        this.type = type;
        this.numberOfSets = numberOfSets;
        this.setDuration = setDuration;
        this.image = image;
    }
}
