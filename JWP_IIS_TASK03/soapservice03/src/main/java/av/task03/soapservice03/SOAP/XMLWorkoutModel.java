package av.task03.soapservice03.SOAP;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@XmlRootElement(name = "Workout")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLWorkoutModel {
    @XmlElement(name = "Name")
    private String name;

    @XmlElement(name = "Instructions")
    private String instructions;

    @XmlElement(name = "Type")
    private WorkoutType type;

    @XmlElement(name = "NumberOfSets")
    private Integer numberOfSets;

    @XmlElement(name = "SetDuration")
    private Integer setDuration;

    @XmlElement(name = "Content")
    private String content;
}

