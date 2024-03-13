package av.iisproject.restapi.DTO;

import av.iisproject.restapi.DAL.Entity.Image;
import av.iisproject.restapi.DAL.Enum.WorkoutType;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class DTOWorkoutResponse {
    private Long workoutId;
    private String name;
    private String instructions;
    private WorkoutType type;
    private Integer numberOfSets;
    private Integer setDuration;
    private Image image;

    public DTOWorkoutResponse(String name, String instructions, WorkoutType type, Integer numberOfSets, Integer setDuration, Image image) {
        this.name = name;
        this.instructions = instructions;
        this.type = type;
        this.numberOfSets = numberOfSets;
        this.setDuration = setDuration;
        this.image = image;
    }
}
