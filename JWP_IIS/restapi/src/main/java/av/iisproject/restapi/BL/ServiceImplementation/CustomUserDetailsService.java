package av.iisproject.restapi.BL.ServiceImplementation;

import av.iisproject.restapi.Config.SecurityConfig;
import av.iisproject.restapi.DAL.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import  org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private final UserRepository userRepository;
    private final SecurityServiceImpl securityService;

    public CustomUserDetailsService(UserRepository userRepository, SecurityServiceImpl securityService) {
        this.userRepository = userRepository;
        this.securityService = securityService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<av.iisproject.restapi.DAL.Entity.User> existingUser = userRepository.findByUsernameWithAuthorities(username);
        if (existingUser != null) {
            List<SimpleGrantedAuthority> authorities = existingUser.get().getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getAuthority().toString()))
                    .collect(Collectors.toList());
            logger.info("AUTHORITIES" + authorities.toString());
            return new User(existingUser.get().getUsername(), existingUser.get().getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }



}
