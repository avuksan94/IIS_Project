package av.task07.interoperability.MVC;

import av.task07.interoperability.DAL.Entity.DHMZWeather;
import av.task07.interoperability.DAL.ProjConsts.EndpointConstants;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping("weatherMvc")
public class WeatherController {
    private final RestTemplate restTemplate;

    public WeatherController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("allCities")
    public String getAllWorkouts(Model model) {

        try {
            ResponseEntity<DHMZWeather[]> response = restTemplate.exchange(
                    EndpointConstants.DHMZ_REST_API,
                    HttpMethod.GET,
                    null,
                    DHMZWeather[].class);

            List<DHMZWeather> weatherForecast = Arrays.asList(response.getBody());
            model.addAttribute("weatherForecast", weatherForecast);
            return "weather/list-weather";
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to fetch all weather reports", e);
        }
    }

    @GetMapping("/cityWeather/{cityName}")
    public String getWeatherByCity(@PathVariable String cityName, Model model) {
        final String url = EndpointConstants.DHMZ_RPC + cityName;
        try {
            ResponseEntity<DHMZWeather> response = restTemplate.getForEntity(url, DHMZWeather.class);
            DHMZWeather weather = response.getBody();
            model.addAttribute("weather", weather);
            return "weather/show-weather";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to fetch weather data for " + cityName + ": " + e.getMessage());
            return "error";
        }
    }
}
