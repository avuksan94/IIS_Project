package av.iisproject.restapi.DAL.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@ToString
public class User {
    @Id
    @Column(name = "username", length = 255, unique = true, nullable = false)
    @NotEmpty(message = "Username is required")
    private String username;

    @Column(name = "password", length = 128, nullable = false)
    @NotEmpty(message = "Password is required")
    private String password;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    //https://stackoverflow.com/questions/2990799/difference-between-fetchtype-lazy-and-eager-in-java-persistence-api
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Authority> authorities = new HashSet<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, Set<Authority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public User(String username, String password, boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public User(String password, boolean enabled, Set<Authority> authorities) {
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
    }
}