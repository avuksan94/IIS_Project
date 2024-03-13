package av.iisproject.restapi.BL.ServiceImplementation;

import av.iisproject.restapi.BL.Service.WorkoutService;
import av.iisproject.restapi.DAL.Entity.Workout;
import av.iisproject.restapi.DAL.Repository.WorkoutRepository;
import av.iisproject.restapi.Utils.CustomNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkoutServiceImplementation implements WorkoutService {
    private final WorkoutRepository workoutRepository;

    public WorkoutServiceImplementation(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    @Override
    public List<Workout> findAll() {
        return workoutRepository.findAll();
    }

    @Override
    public Workout findById(long id) {
        Optional<Workout> workoutOptional = workoutRepository.findById(id);

        if (workoutOptional.isEmpty()){
            throw new CustomNotFoundException("Workout id not found - " + id);
        }
        return workoutOptional.get();
    }

    @Override
    @Transactional
    public Workout save(Workout obj) {
        return workoutRepository.save(obj);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Optional<Workout> checkIfExists = workoutRepository.findById(id);
        if (checkIfExists.isEmpty()){
            throw new CustomNotFoundException("Workout with that ID was not found: " + id);
        }
        workoutRepository.deleteById(id);
    }
}
