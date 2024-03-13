package av.task02.xmlservice02.Rest;

import av.task02.xmlservice02.BL.RelaxNGValidationService;
import av.task02.xmlservice02.DAL.Entity.XMLWorkoutModel;
import av.task02.xmlservice02.DAL.Entity.XMLWorkoutResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/apiRNG/workout")
public class WorkoutRestController {
    private final RelaxNGValidationService xmlService;
    private final RestTemplate restTemplate;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;


    public WorkoutRestController(RelaxNGValidationService xmlService, RestTemplate restTemplate, ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.xmlService = xmlService;
        this.restTemplate = restTemplate;
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> echoXmlUserAndSendJson(@RequestBody XMLWorkoutModel workout, @RequestHeader("Authorization") String authToken) {
        File tempFile = null;
        File schemaFile = null;
        try {
            tempFile = File.createTempFile("workout-", ".xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(XMLWorkoutModel.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(workout, new FileOutputStream(tempFile));

            //look in my resources folder
            Resource resource = resourceLoader.getResource("classpath:workout.rng");
            InputStream schemaInputStream = resource.getInputStream();
            schemaFile = File.createTempFile("schema-", ".rng");
            Files.copy(schemaInputStream, schemaFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            boolean fileIsValid = xmlService.validate(tempFile, schemaFile.getAbsolutePath());
            if (!fileIsValid) {
                return ResponseEntity.badRequest().body("Invalid XML file.");
            }

            // XMLWorkout ----> JSON request
            ObjectMapper jsonMapper = new ObjectMapper();
            String json = jsonMapper.writeValueAsString(workout);

            //Sending the json for creation(need to include my token)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", authToken);
            HttpEntity<String> entity = new HttpEntity<>(json, headers);
            String apiUrl = "http://localhost:8080/workout/workout";
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

            // JSON response(content type for my API is JSON) ---->  XMLWorkoutResponse
            XMLWorkoutResponse createdWorkoutResponse = objectMapper.readValue(response.getBody(), XMLWorkoutResponse.class);

            // XMLWorkoutResponse ---> XML string
            jaxbContext = JAXBContext.newInstance(XMLWorkoutResponse.class);
            marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter writer = new StringWriter();
            marshaller.marshal(createdWorkoutResponse, writer);
            String createdWorkoutXml = writer.toString();

            return ResponseEntity.status(HttpStatus.CREATED).body(createdWorkoutXml);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the workout data: " + e.getMessage());
        } finally {
            if (tempFile != null) tempFile.delete();
            if (schemaFile != null) schemaFile.delete();
        }
    }
}
