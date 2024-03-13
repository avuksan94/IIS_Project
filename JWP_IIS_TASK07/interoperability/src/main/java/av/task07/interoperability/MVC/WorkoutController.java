package av.task07.interoperability.MVC;

import av.task07.interoperability.DAL.Entity.JwtResponse;
import av.task07.interoperability.DAL.Entity.XMLWorkoutModel;
import av.task07.interoperability.DAL.Enum.WorkoutType;
import av.task07.interoperability.DAL.ProjConsts.*;
import av.task07.interoperability.VM.ImageViewModel;
import av.task07.interoperability.VM.WorkoutViewModel;
import av.task07.interoperability.VM.WorkoutViewModelCreate;
import jakarta.validation.Valid;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static av.task07.interoperability.DAL.ProjConsts.EndpointConstants.ALL_WORKOUTS_API;

@Controller
@RequestMapping("workoutMvc")
public class WorkoutController {
    private final RestTemplate restTemplate;
    public WorkoutController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String getAuthToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof String) {
            String jwtToken = (String) authentication.getDetails();
            return jwtToken;
        }
        return " ";
    }

    @GetMapping("allWorkouts")
    public String getAllWorkouts(Model model) {

        String token = getAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<WorkoutViewModel[]> response = restTemplate.exchange(
                    ALL_WORKOUTS_API, HttpMethod.GET, entity, WorkoutViewModel[].class);

            List<WorkoutViewModel> workouts = Arrays.asList(response.getBody());
            model.addAttribute("workouts", workouts);
            return "workout/list-workout";
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to fetch workouts", e);
        }
    }

    @GetMapping("showFormForAddWorkoutXSD")
    public String showFormForAddWorkoutXSD(Model theModel){
        XMLWorkoutModel workout =  new XMLWorkoutModel();
        theModel.addAttribute("workout", workout);
        return "workout/workout-form";
    }

    @GetMapping("showFormForAddWorkoutRNG")
    public String showFormForAddWorkoutRNG(Model theModel){
        XMLWorkoutModel workout =  new XMLWorkoutModel();
        theModel.addAttribute("workout", workout);
        return "workout/workout-form-rng";
    }

    @PostMapping("saveWithXSDValidation")
    public String saveWorkoutXSD(@Valid @ModelAttribute("workout") WorkoutViewModelCreate workout, BindingResult bindingResult, Model model) {
        if (hasFormErrors(bindingResult, model)) {
            return "workout/workout-form";
        }

        if (workoutNameExists(workout.getName())) {
            model.addAttribute("errorMessage", "Workout with that name already exists!");
            return "workout/workout-form";
        }

        try {
            String xmlString = convertWorkoutToXml(new XMLWorkoutModel(
                    workout.getName(),
                    workout.getInstructions(),
                    workout.getType(),
                    workout.getNumberOfSets(),
                    workout.getSetDuration(),
                    workout.getContent()));

            sendWorkout(xmlString,EndpointConstants.POST_WITH_XSD);

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

        return "redirect:/workoutMvc/allWorkouts";
    }

    @PostMapping("saveWithRNGValidation")
    public String saveWorkoutRNG(@Valid @ModelAttribute("workout") WorkoutViewModelCreate workout, BindingResult bindingResult, Model model) {
        if (hasFormErrors(bindingResult, model)) {
            return "workout/workout-form";
        }

        if (workoutNameExists(workout.getName())) {
            model.addAttribute("errorMessage", "Workout with that name already exists!");
            return "workout/workout-form";
        }

        try {
            String xmlString = convertWorkoutToXml(new XMLWorkoutModel(
                    workout.getName(),
                    workout.getInstructions(),
                    workout.getType(),
                    workout.getNumberOfSets(),
                    workout.getSetDuration(),
                    workout.getContent()));

            sendWorkout(xmlString,EndpointConstants.POST_WITH_RNG);

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

        return "redirect:/workoutMvc/allWorkouts";
    }

    private boolean hasFormErrors(BindingResult bindingResult, Model model) {
        return bindingResult.hasErrors();
    }

    private boolean workoutNameExists(String workoutName) {
        String token = getAuthToken();
        HttpHeaders checkHeaders = new HttpHeaders();
        checkHeaders.set("Authorization", "Bearer " + token);
        HttpEntity<String> checkEntity = new HttpEntity<>(checkHeaders);

        ResponseEntity<WorkoutViewModel[]> response = restTemplate.exchange(
                ALL_WORKOUTS_API, HttpMethod.GET, checkEntity, WorkoutViewModel[].class);

        return Arrays.stream(response.getBody())
                .anyMatch(workM -> workM.getName().equals(workoutName));
    }

    private String convertWorkoutToXml(XMLWorkoutModel xmlWorkoutModel) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(XMLWorkoutModel.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter writer = new StringWriter();
        marshaller.marshal(xmlWorkoutModel, writer);
        return writer.toString();
    }

    private void sendWorkout(String xmlWorkout, String postAPI) {
        String token = getAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(xmlWorkout, headers);

        restTemplate.exchange(postAPI, HttpMethod.POST, entity, String.class);
    }


    //TASK03
    @GetMapping("findByType")
    public String findByType(Model theModel, @RequestParam("keyword") String keyword) {
        String soapXml = createSoapRequest(keyword.toUpperCase());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);
        headers.set("SOAPAction", "#POST");

        HttpEntity<String> request = new HttpEntity<>(soapXml, headers);
        ResponseEntity<String> response = restTemplate.exchange(EndpointConstants.SOAP_ENDPOINT, HttpMethod.POST, request, String.class);
        processSoapResponse(response.getBody(), theModel);

        return "workout/list-workout";
    }

    private String createSoapRequest(String keyword) {
        StringBuilder soapRequest = new StringBuilder();

        soapRequest.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" ")
                .append("xmlns:tns=\"http://avtest.com/soap-web-service\">")
                .append("<soapenv:Header/>")
                .append("<soapenv:Body>")
                .append("<tns:WorkoutRequest>")
                .append("<tns:searchCriteria>").append(keyword).append("</tns:searchCriteria>")
                .append("</tns:WorkoutRequest>")
                .append("</soapenv:Body>")
                .append("</soapenv:Envelope>");

        return soapRequest.toString();
    }

    private void processSoapResponse(String response, Model theModel) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(response));
            Document document = builder.parse(is);

            NodeList nodeList = document.getElementsByTagName("workout");
            List<WorkoutViewModel> workouts = new ArrayList<>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    WorkoutViewModel workout = new WorkoutViewModel();

                    workout.setWorkoutId(Long.valueOf(element.getElementsByTagName("workoutId").item(0).getTextContent()));
                    workout.setName(element.getElementsByTagName("Name").item(0).getTextContent());
                    workout.setInstructions(element.getElementsByTagName("Instructions").item(0).getTextContent());
                    workout.setType(WorkoutType.valueOf(element.getElementsByTagName("Type").item(0).getTextContent()));
                    workout.setNumberOfSets(Integer.valueOf(element.getElementsByTagName("NumberOfSets").item(0).getTextContent()));
                    workout.setSetDuration(Integer.valueOf(element.getElementsByTagName("SetDuration").item(0).getTextContent()));

                    ImageViewModel image = new ImageViewModel(element.getElementsByTagName("ImageContent").item(0).getTextContent());
                    workout.setImage(image);

                    workouts.add(workout);
                }
            }

            theModel.addAttribute("workouts", workouts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
