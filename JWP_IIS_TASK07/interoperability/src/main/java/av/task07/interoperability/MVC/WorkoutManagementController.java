package av.task07.interoperability.MVC;


import av.task07.interoperability.DAL.Entity.XMLWorkoutModel;
import av.task07.interoperability.DAL.ProjConsts.EndpointConstants;
import av.task07.interoperability.VM.WorkoutViewModel;
import av.task07.interoperability.VM.WorkoutViewModelCreate;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static av.task07.interoperability.DAL.ProjConsts.EndpointConstants.ALL_WORKOUTS_API;

@Controller
@RequestMapping("workoutManagementMvc")
public class WorkoutManagementController {
    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(WorkoutManagementController.class);
    public WorkoutManagementController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String getAuthToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof String) {
            String jwtToken = (String) authentication.getDetails();
            return jwtToken;
        }
        return " ";
    }

    @GetMapping("allWorkouts")
    public String getAllWorkouts(Model model) {

        String token = getAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<WorkoutViewModel[]> response = restTemplate.exchange(
                    ALL_WORKOUTS_API, HttpMethod.GET, entity, WorkoutViewModel[].class);

            List<WorkoutViewModel> workouts = Arrays.asList(response.getBody());
            model.addAttribute("workouts", workouts);
            return "workout/list-workout-admin";
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to fetch workouts", e);
        }
    }

    @GetMapping("showFormForAddWorkout")
    public String showFormForAddWorkout(Model theModel){
        WorkoutViewModelCreate workout = new WorkoutViewModelCreate();
        theModel.addAttribute("workout", workout);

        return "workout/workout-form-api";
    }

    @PostMapping("save")
    public String saveWorkout(@Valid @ModelAttribute("workout") WorkoutViewModelCreate workout, BindingResult bindingResult, Model model) {
        if (hasFormErrors(bindingResult, model)) {
            return "workout/workout-form-api";
        }

        if (workoutNameExists(workout.getName())) {
            model.addAttribute("errorMessage", "Workout with that name already exists!");
            return "workout/workout-form-api";
        }

        try {
            sendWorkout(workout, EndpointConstants.POST_WORKOUTS_API);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

        return "redirect:/workoutManagementMvc/allWorkouts";
    }

    private boolean hasFormErrors(BindingResult bindingResult, Model model) {
        return bindingResult.hasErrors();
    }

    private boolean workoutNameExists(String workoutName) {
        String token = getAuthToken();
        HttpHeaders checkHeaders = new HttpHeaders();
        checkHeaders.set("Authorization", "Bearer " + token);
        HttpEntity<String> checkEntity = new HttpEntity<>(checkHeaders);

        ResponseEntity<WorkoutViewModel[]> response = restTemplate.exchange(
                ALL_WORKOUTS_API, HttpMethod.GET, checkEntity, WorkoutViewModel[].class);

        return Arrays.stream(response.getBody())
                .anyMatch(workM -> workM.getName().equals(workoutName));
    }

    private void sendWorkout(Object workout, String postAPI) {
        String token = getAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Object> entity = new HttpEntity<>(workout, headers);

        ResponseEntity<String> response = restTemplate.exchange(postAPI, HttpMethod.POST, entity, String.class);
        logger.info(response.getStatusCode().toString()); // Log status code
        logger.info(response.getBody()); // Log response body
    }

    @GetMapping("delete")
    public String delete(@RequestParam("workoutId") int theId) {
        String token = getAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String deleteUrl = EndpointConstants.DELETE_WORKOUTS_API + theId;

        try {
            ResponseEntity<String> response = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, entity, String.class);
            logger.info(response.getStatusCode().toString()); // Log status code
            logger.info(response.getBody()); // Log response body
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/workoutManagementMvc/allWorkouts";
    }

    @GetMapping("showFormForUpdateWorkout")
    public String showFormForUpdateWorkout(@RequestParam("workoutId") int theId, Model theModel){
        String token = getAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String workoutByIdUrl = EndpointConstants.GET_BY_ID_WORKOUTS_API + theId;

        try {
            ResponseEntity<WorkoutViewModelCreate> response = restTemplate.exchange(workoutByIdUrl, HttpMethod.GET, entity, WorkoutViewModelCreate.class);

            if(response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                WorkoutViewModelCreate workout = response.getBody();
                theModel.addAttribute("workout", workout);
                return "workout/workout-form-api";
            } else {
                return "error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
