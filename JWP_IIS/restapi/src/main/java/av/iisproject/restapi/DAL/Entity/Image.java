package av.iisproject.restapi.DAL.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="image")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@ToString
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_Id")
    private Long imageId;

    @Column(name = "content", nullable = false)
    private String content;
}