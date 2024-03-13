package av.iisproject.restapi.BL.Service;

import av.iisproject.restapi.DAL.Entity.Image;
import av.iisproject.restapi.DAL.Entity.Workout;

import java.util.List;

public interface WorkoutService {
    List<Workout> findAll();
    Workout findById(long id);
    Workout save(Workout obj);
    void deleteById(long id);
}
