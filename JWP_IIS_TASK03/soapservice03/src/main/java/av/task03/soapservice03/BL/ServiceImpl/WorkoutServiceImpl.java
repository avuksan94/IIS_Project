package av.task03.soapservice03.BL.ServiceImpl;

import av.task03.soapservice03.SOAP.*;
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
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//https://learning.postman.com/docs/sending-requests/soap/making-soap-requests/

@Endpoint
public class WorkoutServiceImpl implements WorkoutService {
    private static final Logger log = LoggerFactory.getLogger(WorkoutServiceImpl.class);
    private final XMLValidationService xmlValidationService;
    private static final String NAMESPACE_URI = "http://avtest.com/soap-web-service";
    private static final String WORKOUT_API_GET_ALL= "http://localhost:8080/workout/allWorkouts";
    private final String AUTH_LOGIN = "http://localhost:8080/auth/login";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    private final RestTemplate restTemplate;

    public WorkoutServiceImpl(XMLValidationService xmlValidationService, RestTemplate restTemplate) {
        this.xmlValidationService = xmlValidationService;
        log.debug("WorkoutServiceImpl instantiated");
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

    public List<XMLWorkoutResponse> fetchAllWorkouts() {
        String token = getAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<XMLWorkoutResponse>> response = restTemplate.exchange(
                WORKOUT_API_GET_ALL,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<XMLWorkoutResponse>>() {}
        );
        return response.getBody();
    }

    //https://stackoverflow.com/questions/14390474/no-adapter-for-endpoint-is-your-endpoint-annotated-with-endpoint-or-does-it-i
    //need to use JAXBElement
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "WorkoutRequest")
    @ResponsePayload
    public JAXBElement<WorkoutResponse> getWorkoutDetails(@RequestPayload JAXBElement<WorkoutRequest> requestElement) throws Exception {
        WorkoutRequest request = requestElement.getValue();
        log.debug("getWorkoutDetails called with searchCriteria: {}", request.getSearchCriteria());

        List<XMLWorkoutResponse> allWorkouts = fetchAllWorkouts();
        JAXBContext context = JAXBContext.newInstance(XMLWorkoutResponse.class, WorkoutRequest.class, WorkoutResponse.class);
        Document document = convertToXmlDocument(allWorkouts, context);


        writeWorkoutsToFile(allWorkouts);
        validateAllWorkouts();

        //https://www.baeldung.com/java-xpath

        String expression = String.format("//Workout[Name/text()='%s' or Type/text()='%s']",
                request.getSearchCriteria(), request.getSearchCriteria());
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr = xpath.compile(expression);
        NodeList workoutNodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        List<XMLWorkoutResponse> workoutResponses = new ArrayList<>();
        for (int i = 0; i < workoutNodes.getLength(); i++) {
            Node workoutNode = workoutNodes.item(i);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            XMLWorkoutResponse workoutResponse = (XMLWorkoutResponse) unmarshaller.unmarshal(workoutNode);
            workoutResponses.add(workoutResponse);
        }
        WorkoutResponse response = new WorkoutResponse();
        response.setWorkouts(workoutResponses);

        //https://docs.oracle.com/javase/8/docs/api/javax/xml/namespace/QName.html
        QName qName = new QName(NAMESPACE_URI, "WorkoutResponse");
        JAXBElement<WorkoutResponse> responseElement = new JAXBElement<>(qName, WorkoutResponse.class, response);

        return responseElement;
    }

    private void removeNamespaces(Document document) {
        NodeList elements = document.getElementsByTagName("*");
        for (int i = 0; i < elements.getLength(); i++) {
            org.w3c.dom.Node node = elements.item(i);
            if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                document.renameNode(node, null, node.getLocalName());
            }
        }
    }



    //Need this for TASK04 also
    private Document convertToXmlDocument(List<XMLWorkoutResponse> allWorkouts, JAXBContext context) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Node rootElement = document.createElement("Workouts");
        document.appendChild(rootElement);

        //convert objects to XML
        Marshaller marshaller = context.createMarshaller();
        for (XMLWorkoutResponse workout : allWorkouts) {
            marshaller.marshal(workout, rootElement);
        }
        return document;
    }

    /*
    private void writeDocumentToFile(Document document) throws Exception {
        try {
            String relativePath = "soapservice03" + File.separator + "xmldata" + File.separator + "allWorkouts.xml";

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(relativePath));
            transformer.transform(source, result);

            log.info("Generated XML file at {}", new File(relativePath).getAbsolutePath());
        } catch (Exception e) {
            log.error("Error writing document to file", e);
        }
    }
     */

    public void writeWorkoutsToFile(List<XMLWorkoutResponse> allWorkouts) throws JAXBException {
        String relativePath = "soapservice03" + File.separator + "xmldata" + File.separator + "allWorkouts.xml";

        WorkoutsWrapper wrapper = new WorkoutsWrapper();
        wrapper.setWorkouts(allWorkouts);

        JAXBContext context = JAXBContext.newInstance(WorkoutsWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        File file = new File(relativePath);
        marshaller.marshal(wrapper, file);

        System.out.println("XML file created: " + file.getAbsolutePath());
    }

    public void validateAllWorkouts() {
        String xmlFilePath = "soapservice03" + File.separator + "xmldata" + File.separator + "allWorkouts.xml";
        String xsdPath = "workoutResponse.xsd";
        try {
            boolean isValid = xmlValidationService.isValid(xmlFilePath, xsdPath);
            if (isValid) {
                log.info("XML is valid against the XSD");
            } else {
                log.info("XML is not valid against the XSD");
            }
        } catch (IOException | SAXException e) {
            log.error("Validation failed", e);
        }
    }


}
