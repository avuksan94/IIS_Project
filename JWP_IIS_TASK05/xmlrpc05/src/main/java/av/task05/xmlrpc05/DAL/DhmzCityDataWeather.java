package av.task05.xmlrpc05.DAL;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class DhmzCityDataWeather extends CityWeather implements Serializable {
    private double temperature;
    private int humidity;
    private double pressure;
    private double pressureTrend;
    private String windDirection;
    private double windSpeed;
    private String weatherDescription;

    private int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private double parseDouble(String value, double defaultValue) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public DhmzCityDataWeather(
            String cityName, String temperature,
            String humidity, String pressure, String pressureTrend,
            String windDirection, String windSpeed, String weatherDescription) {
        super(cityName);
        this.temperature = parseDouble(temperature, 0.0);
        this.humidity = parseInt(humidity, 0);
        this.pressure = parseDouble(pressure, 0.0);
        this.pressureTrend = parseDouble(pressureTrend, 0.0);
        this.windDirection = windDirection;
        this.windSpeed = parseDouble(windSpeed, 0.0);
        this.weatherDescription = weatherDescription;

    }

    public DhmzCityDataWeather(String cityName, String temperature) {
        super(cityName);
        this.temperature = parseDouble(temperature, 0.0);
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"error\":\"Error processing JSON\"}";
        }
    }
}