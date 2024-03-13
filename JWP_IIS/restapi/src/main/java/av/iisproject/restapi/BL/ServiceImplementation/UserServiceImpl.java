package av.iisproject.restapi.BL.ServiceImplementation;

import av.iisproject.restapi.BL.Service.SecurityService;
import av.iisproject.restapi.BL.Service.UserService;
import av.iisproject.restapi.DAL.Entity.Authority;
import av.iisproject.restapi.DAL.Entity.User;
import av.iisproject.restapi.DAL.Enum.Role;
import av.iisproject.restapi.DAL.Repository.AuthorityRepository;
import av.iisproject.restapi.DAL.Repository.UserRepository;
import av.iisproject.restapi.Utils.CustomNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final AuthorityRepository authorityRepo;
    private final SecurityService securityService;

    public UserServiceImpl( UserRepository userRepo, AuthorityRepository authorityRepo, SecurityService securityService) {
        this.userRepo = userRepo;
        this.authorityRepo = authorityRepo;
        this.securityService = securityService;
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    @Transactional
    public User findByUsername(String username) {
        Optional<User> userOptional = Optional.ofNullable(userRepo.findByUsername(username));

        if (userOptional.isEmpty()){
            throw new CustomNotFoundException("User with that username not found - " + username);
        }

        return userOptional.get();
    }

    @Override
    @Transactional
    public User save(User obj) {
        return userRepo.save(obj);
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        userRepo.deleteByUsername(username);
    }

    @Override
    @Transactional
    public void createAdminUser(String username, String rawPassword) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(securityService.doBCryptPassEncoding(rawPassword));
        user.setEnabled(true);
        userRepo.save(user);

        Authority authority = new Authority();
        authority.setUser(user);
        authority.setAuthority(Role.ROLE_ADMIN);
        authorityRepo.save(authority);
    }

    @Override
    public void createAPIUser(String username, String rawPassword) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(securityService.doBCryptPassEncoding(rawPassword));
        user.setEnabled(true);
        userRepo.save(user);

        Authority authority = new Authority();
        authority.setUser(user);
        authority.setAuthority(Role.ROLE_API_USER);
        authorityRepo.save(authority);
    }

    @Override
    public Optional<User> findByUsernameWithAuthorities(String username) {
        return userRepo.findByUsernameWithAuthorities(username);
    }
}
