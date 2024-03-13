package av.task03.soapservice03.SOAP;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WorkoutResponse", propOrder = {
        "workouts"
})
@Getter
@Setter
public class WorkoutResponse {

    @XmlElementWrapper(name = "workouts")
    @XmlElement(name = "workout")
    private List<XMLWorkoutResponse> workouts;
}
