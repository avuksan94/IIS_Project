package av.task03.soapservice03.SOAP;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WorkoutRequest", propOrder = {
        "searchCriteria"
})
@Getter
@Setter
public class WorkoutRequest {

    @XmlElement(name = "searchCriteria", namespace = "http://avtest.com/soap-web-service")
    private String searchCriteria;
}
