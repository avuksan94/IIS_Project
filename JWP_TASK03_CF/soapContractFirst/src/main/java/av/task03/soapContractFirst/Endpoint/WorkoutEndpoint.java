package av.task03.soapContractFirst.Endpoint;

import av.task03.soapContractFirst.JwtResponse;
import com.avtest.soap_web_servicecf.WorkoutRequestType;
import com.avtest.soap_web_servicecf.WorkoutResponseType;
import com.avtest.soap_web_servicecf.XMLWorkoutResponseType;
import jakarta.xml.bind.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Endpoint
public class WorkoutEndpoint {
    private static final Logger log = LoggerFactory.getLogger(WorkoutEndpoint.class);
    private static final String NAMESPACE_URI = "http://avtest.com/soap-web-serviceCF";
    private static final String WORKOUT_API_GET_ALL= "http://localhost:8080/workout/allWorkouts";
    private final String AUTH_LOGIN = "http://localhost:8080/auth/login";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    private final RestTemplate restTemplate;


    public WorkoutEndpoint(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String getAuthToken() {
        Map<String, String> credentials = Map.of("username", USERNAME, "password", PASSWORD);
        try {
            ResponseEntity<JwtResponse> response = restTemplate.postForEntity(
                    AUTH_LOGIN,
                    credentials,
                    JwtResponse.class
            );
            JwtResponse jwtResponse = response.getBody();
            return jwtResponse != null ? jwtResponse.getToken() : null;
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Authentication failed", e);
        }
    }

    public List<XMLWorkoutResponseType> fetchAllWorkouts() {
        String token = getAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<XMLWorkoutResponseType>> response = restTemplate.exchange(
                WORKOUT_API_GET_ALL,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<XMLWorkoutResponseType>>() {}
        );
        return response.getBody();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "WorkoutRequest")
    @ResponsePayload
    public JAXBElement<WorkoutResponseType> getWorkoutDetailsByName(@RequestPayload JAXBElement<WorkoutRequestType> requestElement) throws Exception {
        WorkoutRequestType request = requestElement.getValue();
        log.debug("getWorkoutDetailsByName called with searchCriteria: {}", request.getSearchCriteria());

        List<XMLWorkoutResponseType> allWorkouts = fetchAllWorkouts();
        JAXBContext context = JAXBContext.newInstance(XMLWorkoutResponseType.class, WorkoutRequestType.class, WorkoutResponseType.class);
        Document document = convertToXmlDocument(allWorkouts, context);

        String expression = String.format("//Workout[Name/text()='%s']", request.getSearchCriteria());
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr = xpath.compile(expression);
        NodeList workoutNodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        List<XMLWorkoutResponseType> workoutResponses = new ArrayList<>();
        for (int i = 0; i < workoutNodes.getLength(); i++) {
            Node workoutNode = workoutNodes.item(i);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            XMLWorkoutResponseType workoutResponse = (XMLWorkoutResponseType) unmarshaller.unmarshal(workoutNode);
            workoutResponses.add(workoutResponse);
        }

        WorkoutResponseType response = new WorkoutResponseType();
        response.setWorkouts(workoutResponses);

        QName qName = new QName(NAMESPACE_URI, "WorkoutResponse");
        JAXBElement<WorkoutResponseType> responseElement = new JAXBElement<>(qName, WorkoutResponseType.class, response);

        return responseElement;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "WorkoutRequestAll")
    @ResponsePayload
    public JAXBElement<WorkoutResponseType> getAllWorkouts(@RequestPayload JAXBElement<WorkoutRequestType> requestElement) throws Exception {
        List<XMLWorkoutResponseType> allWorkouts = fetchAllWorkouts();

        WorkoutResponseType response = new WorkoutResponseType();
        response.setWorkouts(allWorkouts);

        QName qName = new QName(NAMESPACE_URI, "WorkoutResponse");
        JAXBElement<WorkoutResponseType> responseElement = new JAXBElement<>(qName, WorkoutResponseType.class, response);

        return responseElement;
    }




    //Need this for TASK04 also
    private Document convertToXmlDocument(List<XMLWorkoutResponseType> allWorkouts, JAXBContext context) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Node rootElement = document.createElement("Workouts");
        document.appendChild(rootElement);

        //convert objects to XML
        Marshaller marshaller = context.createMarshaller();
        for (XMLWorkoutResponseType workout : allWorkouts) {
            marshaller.marshal(workout, rootElement);
        }
        return document;
    }
}
