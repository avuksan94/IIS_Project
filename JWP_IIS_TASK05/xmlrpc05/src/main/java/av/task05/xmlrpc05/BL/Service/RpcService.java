package av.task05.xmlrpc05.BL.Service;

import av.task05.xmlrpc05.DAL.CityWeather;
import av.task05.xmlrpc05.DAL.DhmzCityDataWeather;

import java.util.List;

public interface RpcService {
    List<CityWeather> getCities();
    List<DhmzCityDataWeather> getDhmzCity(String cityName);

}
