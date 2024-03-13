package av.task05.xmlrpc05.BL.ServiceImpl;

import av.task05.xmlrpc05.BL.Service.RpcService;
import av.task05.xmlrpc05.DAL.CityWeather;
import av.task05.xmlrpc05.DAL.DhmzCityDataWeather;

import java.util.List;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

//https://sivakumar-prasanth.medium.com/java-rpc-remote-procedure-call-99cfaca34c36
/*
A remote procedure call is an inter-process communication technique that is used for client-server based applications.
It is also known as a subroutine call or a function call.

A client has a request message that the RPC translates and sends to the server.
This request may be a procedure or a function call to a remote server.
When the server receives the request, it sends the required response back to the client.
The client is blocked while the server is processing the call and only resumed execution after the server is finished.
*/

@Service
public class RpcServiceImpl implements RpcService {
    private List<DhmzCityDataWeather> getDataFromDhmzServer() {
        try {
            URL url = new URL("https://vrijeme.hr/hrvatska_n.xml");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                return null;
            }

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(url.openStream());

            List<DhmzCityDataWeather> dhmzCityDataList = new ArrayList<>();
            NodeList gradovi = document.getElementsByTagName("Grad");

            for (int i = 0; i < gradovi.getLength(); i++) {
                Element grad = (Element) gradovi.item(i);
                String gradIme = grad.getElementsByTagName("GradIme").item(0).getTextContent();
                String temp = grad.getElementsByTagName("Temp").item(0).getTextContent();
                String vlaga = grad.getElementsByTagName("Vlaga").item(0).getTextContent();
                String tlak = grad.getElementsByTagName("Tlak").item(0).getTextContent();
                String tlakTend = grad.getElementsByTagName("TlakTend").item(0).getTextContent();
                String vjetarSmjer = grad.getElementsByTagName("VjetarSmjer").item(0).getTextContent();
                String vjetarBrzina = grad.getElementsByTagName("VjetarBrzina").item(0).getTextContent();
                String vrijeme = grad.getElementsByTagName("Vrijeme").item(0).getTextContent();

                dhmzCityDataList.add(new DhmzCityDataWeather(gradIme, temp, vlaga, tlak, tlakTend, vjetarSmjer, vjetarBrzina, vrijeme));
            }

            return dhmzCityDataList;

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<CityWeather> getCities() {
        List<CityWeather> cities = new ArrayList<>();
        for (var city : Objects.requireNonNull(getDataFromDhmzServer())) {
            cities.add(new CityWeather(city.getCityName()));
        }
        return cities;
    }

    @Override
    public List<DhmzCityDataWeather> getDhmzCity(String cityName) {
        List<DhmzCityDataWeather> cityData = new ArrayList<>();
        List<DhmzCityDataWeather> allCitiesData = getDataFromDhmzServer();

        if (allCitiesData == null) return cityData;

        for (DhmzCityDataWeather city : allCitiesData) {
            if (cityName.equalsIgnoreCase(city.getCityName().trim())) {
                cityData.add(city);
            }
        }
        return cityData;
    }


}
