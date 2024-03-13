package av.task01.xmlservice.DAL.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@XmlRootElement(name = "Workout")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLWorkoutResponse {

    @XmlElement(name = "workoutId")
    @JsonProperty("workoutId")
    private Long workoutId;

    @XmlElement(name = "Name")
    private String name;

    @XmlElement(name = "Instructions")
    private String instructions;

    @XmlElement(name = "Type")
    private String type;

    @XmlElement(name = "NumberOfSets")
    private Integer numberOfSets;

    @XmlElement(name = "SetDuration")
    private Integer setDuration;

    @XmlElement(name = "ImageContent")
    private String imageContent;

    @JsonProperty("image")
    private void unpackNestedImageContent(Map<String, Object> image) {
        this.imageContent = (String) image.get("content");
    }
}