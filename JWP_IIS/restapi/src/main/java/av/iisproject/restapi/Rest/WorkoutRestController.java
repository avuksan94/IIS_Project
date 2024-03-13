package av.iisproject.restapi.Rest;

import av.iisproject.restapi.BL.Service.ImageService;
import av.iisproject.restapi.BL.Service.WorkoutService;
import av.iisproject.restapi.DAL.Entity.Image;
import av.iisproject.restapi.DAL.Entity.Workout;
import av.iisproject.restapi.DTO.DTOWorkoutRequest;
import av.iisproject.restapi.DTO.DTOWorkoutResponse;
import av.iisproject.restapi.Mapper.WorkoutRequestMapper;
import av.iisproject.restapi.Mapper.WorkoutResponseMapper;
import av.iisproject.restapi.Utils.CustomNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("workout")
public class WorkoutRestController {
    private final WorkoutService workoutService;
    private final ImageService imageService;
    private final WorkoutRequestMapper workoutRequestMapper;
    private final WorkoutResponseMapper workoutResponseMapper;
    public WorkoutRestController(WorkoutService workoutService, ImageService imageService, WorkoutRequestMapper workoutMapper, WorkoutResponseMapper workoutResponseMapper) {
        this.workoutService = workoutService;
        this.imageService = imageService;
        this.workoutRequestMapper = workoutMapper;
        this.workoutResponseMapper = workoutResponseMapper;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/allWorkouts")
    public List<DTOWorkoutResponse> findAllWorkouts(){
        return
                workoutService.findAll()
                        .stream()
                        .map(workoutResponseMapper::WorkoutToDTOWorkoutRes)
                        .collect(Collectors.toList());
    }

    @Async
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{workoutId}")
    public CompletableFuture<DTOWorkoutResponse> findWorkoutById(@PathVariable int workoutId) {
        Workout workout = workoutService.findById(workoutId);
        if (workout == null) {
            throw new CustomNotFoundException("Workout not found for id: " + workoutId);
        }
        return CompletableFuture.completedFuture(workoutResponseMapper.WorkoutToDTOWorkoutRes(workout));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/workout")
    public DTOWorkoutResponse createWorkout(@Valid @RequestBody DTOWorkoutRequest dtoWorkout) {
        Workout newWorkout = workoutRequestMapper.DTOWorkoutReqToWorkout(dtoWorkout);
        imageService.save(newWorkout.getImage());
        Workout savedWorkout = workoutService.save(newWorkout);
        return workoutResponseMapper.WorkoutToDTOWorkoutRes(savedWorkout);
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/workout/{workoutId}")
    public DTOWorkoutResponse updateWorkout(@PathVariable int workoutId, @Valid @RequestBody DTOWorkoutRequest dtoWorkout){
        Workout updatedWorkout = updateWorkoutHelp(workoutId, dtoWorkout);
        return workoutResponseMapper.WorkoutToDTOWorkoutRes(updatedWorkout);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/workout/{workoutId}")
    public String deleteWorkout(@PathVariable int workoutId){
        Workout workoutToDelete = workoutService.findById(workoutId);
        if (workoutToDelete == null){
            throw new CustomNotFoundException("Workout id not found - " + workoutId);
        }
        Image imageToDelete = workoutToDelete.getImage();
        workoutService.deleteById(workoutId);
        imageService.deleteById(imageToDelete.getImageId());

        return "Deleted workout with ID: " + workoutId;
    }

    public Workout updateWorkoutHelp(int workoutId, DTOWorkoutRequest dtoWorkout) {
        Workout existingWorkout = workoutService.findById(workoutId);
        if (existingWorkout == null) {
            throw new CustomNotFoundException("Workout not found for id: " + workoutId);
        }
        updateWorkoutFields(existingWorkout, dtoWorkout);
        handleImageUpdate(existingWorkout, dtoWorkout.getContent());
        return workoutService.save(existingWorkout);
    }

    private void updateWorkoutFields(Workout workout, DTOWorkoutRequest dto) {
        workout.setName(dto.getName());
        workout.setInstructions(dto.getInstructions());
        workout.setType(dto.getType());
        workout.setNumberOfSets(dto.getNumberOfSets());
        workout.setSetDuration(dto.getSetDuration());
    }

    private void handleImageUpdate(Workout workout, String newContent) {
        Image existingImage = imageService.findById(workout.getImage().getImageId());
        if (!Objects.equals(existingImage.getContent(), newContent)) {
            existingImage.setContent(newContent);
            imageService.save(existingImage);
        }
    }
}
