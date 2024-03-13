package av.iisproject.restapi.DAL.Entity;

import av.iisproject.restapi.DAL.Enum.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "authority")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@ToString
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority", length = 50, nullable = false)
    private Role authority;

    public Authority(User user, Role authority) {
        this.user = user;
        this.authority = authority;
    }
}