package av.task05.xmlrpc05.Rest;

import av.task05.xmlrpc05.BL.ServiceImpl.WeatherClientService;
import av.task05.xmlrpc05.DAL.WeatherResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rpcDhmz")
public class WeatherController {
    private final WeatherClientService weatherClientService;
    @Autowired
    public WeatherController(WeatherClientService weatherClientService) {
        this.weatherClientService = weatherClientService;
    }

    @GetMapping("/weather/{cityName}")
    public ResponseEntity<?> getWeather(@PathVariable String cityName) {
        try {
            String json = weatherClientService.getInfoByCity(cityName);
            ObjectMapper mapper = new ObjectMapper();
            WeatherResponse response = mapper.readValue(json, WeatherResponse.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch weather data: " + e.getMessage());
        }
    }
}
