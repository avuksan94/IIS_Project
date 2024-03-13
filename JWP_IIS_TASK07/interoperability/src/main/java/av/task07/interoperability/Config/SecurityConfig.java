package av.task07.interoperability.Config;

import av.task07.interoperability.Security.ExternalApiAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final ExternalApiAuthenticationProvider externalApiAuthenticationProvider;

    public SecurityConfig(ExternalApiAuthenticationProvider externalApiAuthenticationProvider) {
        this.externalApiAuthenticationProvider = externalApiAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.GET, "/css/**", "/js/**", "/images/**", "/shared/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/security/loginUser").permitAll()
                        .requestMatchers(HttpMethod.POST, "/security/manualLogout").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/security/loginUser")
                        .loginProcessingUrl("/security/security-login") // Corrected URL
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/security/manualLogout")
                        .logoutSuccessUrl("/security/loginUser?logoutManual=true")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable()) // Consider enabling CSRF protection
                .authenticationProvider(externalApiAuthenticationProvider);

        return http.build();
    }
}