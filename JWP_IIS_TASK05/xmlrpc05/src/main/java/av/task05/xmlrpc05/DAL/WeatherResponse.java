package av.task05.xmlrpc05.DAL;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class WeatherResponse {
    private String cityName;
    private String temperature;
    private String humidity;
    private String pressure;
    private String pressureTrend;
    private String windDirection;
    private String windSpeed;
    private String weatherDescription;
}
