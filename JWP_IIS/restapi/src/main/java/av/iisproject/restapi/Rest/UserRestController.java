package av.iisproject.restapi.Rest;

import av.iisproject.restapi.BL.Service.UserService;
import av.iisproject.restapi.DAL.Entity.User;
import av.iisproject.restapi.DAL.Entity.Workout;
import av.iisproject.restapi.DTO.DTOWorkoutRequest;
import av.iisproject.restapi.DTO.DTOWorkoutResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("user")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @Async
    @PostMapping("/addAdminUser")
    public void createAdminUser() {
        userService.createAdminUser("admin","admin");
    }

    @Async
    @PostMapping("/addAPIUser")
    public void createAPIUser() {
        userService.createAPIUser("ante","1234");
    }
}
