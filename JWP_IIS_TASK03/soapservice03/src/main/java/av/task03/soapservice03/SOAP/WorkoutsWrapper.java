package av.task03.soapservice03.SOAP;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "Workouts")
public class WorkoutsWrapper {
    private List<XMLWorkoutResponse> workouts;

    public WorkoutsWrapper() {}

    @XmlElement(name = "Workout")
    public List<XMLWorkoutResponse> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<XMLWorkoutResponse> workouts) {
        this.workouts = workouts;
    }
}
