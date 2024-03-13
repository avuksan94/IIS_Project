package av.iisproject.restapi.Mapper;

import av.iisproject.restapi.DAL.Entity.Workout;
import av.iisproject.restapi.DTO.DTOWorkoutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkoutRequestMapper {
    @Mapping(target = "image.content", source = "content")
    Workout DTOWorkoutReqToWorkout(DTOWorkoutRequest dto);
    @Mapping(target = "content", source = "image.content")
    DTOWorkoutRequest WorkoutToDTOWorkoutReq(Workout workout);
}
