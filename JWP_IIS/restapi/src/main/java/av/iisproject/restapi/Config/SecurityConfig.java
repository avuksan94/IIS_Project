package av.iisproject.restapi.Config;

import av.iisproject.restapi.BL.ServiceImplementation.CustomUserDetailsService;
import av.iisproject.restapi.JWT.JwtRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private final JwtRequestFilter jwtRequestFilter;
    private final ApplicationContext applicationContext;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, ApplicationContext applicationContext) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.applicationContext = applicationContext;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring Security Filter Chain...");
        applicationContext.getAutowireCapableBeanFactory().autowireBean(jwtRequestFilter);
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/webjars/**", "/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/workout/allWorkouts").authenticated()
                        .requestMatchers(HttpMethod.GET, "/workout/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/workout").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/workout/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/workout/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/user/addAPIUser").permitAll()
                        .anyRequest().authenticated())
                // Add JWT filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, CustomUserDetailsService customUserDetailsService) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }

}