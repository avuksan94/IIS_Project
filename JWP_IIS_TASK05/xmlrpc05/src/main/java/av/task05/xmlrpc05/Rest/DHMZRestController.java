package av.task05.xmlrpc05.Rest;

import av.task05.xmlrpc05.BL.Service.RpcService;
import av.task05.xmlrpc05.DAL.CityWeather;
import av.task05.xmlrpc05.DAL.DhmzCityDataWeather;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dhmz")
public class DHMZRestController {
    private final RpcService rpcService;

    public DHMZRestController(RpcService rpcService) {
        this.rpcService = rpcService;
    }

    @GetMapping(value = "/getCities", produces = "application/json")
    public List<CityWeather> getCities() {
        return rpcService.getCities();
    }

    @GetMapping(value = "/getCity", produces = "application/json")
    public List<DhmzCityDataWeather> getDhmzCityData(@RequestParam(name = "city") String cityName) {
        return rpcService.getDhmzCity(cityName);
    }
}
