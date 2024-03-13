package av.iisproject.restapi.DAL.Entity;

import av.iisproject.restapi.DAL.Enum.WorkoutType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="workout")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@ToString
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workout_Id")
    private Long workoutId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "instructions", nullable = false, columnDefinition = "TEXT")
    private String instructions;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private WorkoutType type;

    @Column(name = "number_of_sets", nullable = false)
    private Integer numberOfSets;

    @Column(name = "set_duration", nullable = false)
    private Integer setDuration;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;
}