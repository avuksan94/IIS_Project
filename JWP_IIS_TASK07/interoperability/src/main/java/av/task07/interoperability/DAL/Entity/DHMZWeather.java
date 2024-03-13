package av.task07.interoperability.DAL.Entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class DHMZWeather {
    private String cityName;
    private String temperature;
    private String humidity;
    private String pressure;
    private String pressureTrend;
    private String windDirection;
    private String windSpeed;
    private String weatherDescription;
}