package av.task07.interoperability.Security;

import av.task07.interoperability.DAL.Entity.JwtResponse;
import av.task07.interoperability.DAL.ProjConsts.EndpointConstants;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//https://www.baeldung.com/spring-security-authentication-provider
@Component
public class ExternalApiAuthenticationProvider implements AuthenticationProvider {

    private final RestTemplate restTemplate;

    public ExternalApiAuthenticationProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        JwtResponse jwtResponse = callExternalApiForJwt(username, password);

        if (jwtResponse != null && jwtResponse.getToken() != null) {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    username, password, Collections.emptyList());
            auth.setDetails(jwtResponse.getToken());
            return auth;
        } else {
            throw new BadCredentialsException("External authentication failed");
        }
    }

    private JwtResponse callExternalApiForJwt(String username, String password) {
        final String authUrl = EndpointConstants.AUTH_LOGIN;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> credentials = new HashMap<>();
            credentials.put("username", username);
            credentials.put("password", password);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(credentials, headers);

            ResponseEntity<JwtResponse> response = restTemplate.postForEntity(authUrl, request, JwtResponse.class);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}