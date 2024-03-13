package av.task01.xmlservice.Rest;

import av.task01.xmlservice.BL.XMLService;
import av.task01.xmlservice.DAL.Entity.XMLWorkoutModel;
import av.task01.xmlservice.DAL.Entity.XMLWorkoutResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.io.StringWriter;

@RestController
@RequestMapping("/api/workout")
public class WorkoutRestController {

    private final XMLService xmlService;
    private final RestTemplate restTemplate;

    @Autowired
    public WorkoutRestController(XMLService xmlService, RestTemplate restTemplate) {
        this.xmlService = xmlService;
        this.restTemplate = restTemplate;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> echoXmlUserAndSendJson(@RequestBody XMLWorkoutModel workout, @RequestHeader("Authorization") String authToken) throws Exception {
        boolean fileIsValid = xmlService.isValid(workout, "workout.xsd");
        if (!fileIsValid) {
            throw new Exception("Invalid XML file.");
        }

        ObjectMapper jsonMapper = new ObjectMapper();
        String json = jsonMapper.writeValueAsString(workout);

        String apiUrl = "http://localhost:8080/workout/workout";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authToken);

        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
        XMLWorkoutResponse createdWorkoutResponse = jsonMapper.readValue(response.getBody(), XMLWorkoutResponse.class);

        JAXBContext jaxbContext = JAXBContext.newInstance(XMLWorkoutResponse.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter writer = new StringWriter();
        marshaller.marshal(createdWorkoutResponse, writer);
        String createdWorkoutXml = writer.toString();

        return ResponseEntity.status(HttpStatus.CREATED).body(createdWorkoutXml);
    }
}