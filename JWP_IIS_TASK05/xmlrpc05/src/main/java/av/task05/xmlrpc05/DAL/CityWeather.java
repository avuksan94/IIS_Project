package av.task05.xmlrpc05.DAL;

import lombok.*;

import java.io.Serializable;


@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class CityWeather implements Serializable {
    private String cityName;
}

