package av.iisproject.restapi.Mapper;

import av.iisproject.restapi.DAL.Entity.Workout;
import av.iisproject.restapi.DTO.DTOWorkoutRequest;
import av.iisproject.restapi.DTO.DTOWorkoutResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkoutResponseMapper {
    @Mapping(target = "workoutId", source = "workoutId")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "instructions", source = "instructions")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "numberOfSets", source = "numberOfSets")
    @Mapping(target = "setDuration", source = "setDuration")
    @Mapping(target = "image", source = "image")
    DTOWorkoutResponse WorkoutToDTOWorkoutRes(Workout source);
    @Mapping(target = "workoutId", source = "workoutId")
    @Mapping(target = "instructions", source = "instructions")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "numberOfSets", source = "numberOfSets")
    @Mapping(target = "setDuration", source = "setDuration")
    @Mapping(target = "image", source = "image")
    Workout DTOWorkoutResToWorkout(DTOWorkoutResponse destination);
}
