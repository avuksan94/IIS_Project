package av.iisproject.restapi.BL.Service;

import av.iisproject.restapi.DAL.Entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
    User findByUsername(String username);
    User save(User obj);
    void deleteByUsername(String username);
    void createAdminUser(String username, String rawPassword);
    void createAPIUser(String username, String rawPassword);
    Optional<User> findByUsernameWithAuthorities(String username);
}
