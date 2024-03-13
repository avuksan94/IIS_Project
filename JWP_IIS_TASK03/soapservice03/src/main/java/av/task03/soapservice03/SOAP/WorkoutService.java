package av.task03.soapservice03.SOAP;

import jakarta.jws.WebMethod;
import jakarta.xml.bind.JAXBElement;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;

public interface WorkoutService {
    @WebMethod
    JAXBElement<WorkoutResponse> getWorkoutDetails(@RequestPayload JAXBElement<WorkoutRequest> requestElement) throws Exception;
}
