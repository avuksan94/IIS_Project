package av.task05.xmlrpc05.BL.ServiceImpl;
import av.task05.xmlrpc05.DAL.DhmzCityDataWeather;
import av.task05.xmlrpc05.DAL.WeatherResponse;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Vector;

@Service
public class WeatherClientService {

    public String getInfoByCity(String cityName) {
        try {
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL("http://localhost:9095/xmlrpc"));
            config.setEnabledForExtensions(true);

            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            Vector<String> params = new Vector<>();
            params.addElement(cityName);
            Object[] result = (Object[]) client.execute("RpcService.getDhmzCity", params);

            StringBuilder temperatures = new StringBuilder();
            for (Object temperature : result) {
                if (temperature instanceof DhmzCityDataWeather) {
                    DhmzCityDataWeather weather = (DhmzCityDataWeather) temperature;
                    temperatures.append(weather.getCityName()).append(": ").append(weather.getTemperature()).append("\n");
                } else {
                    temperatures.append(temperature.toString()).append("\n");
                }
            }

            return temperatures.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

}