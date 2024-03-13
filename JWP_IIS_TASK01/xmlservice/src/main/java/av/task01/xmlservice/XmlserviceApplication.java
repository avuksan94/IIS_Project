package av.task01.xmlservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan(basePackages = {
		"av.task01.xmlservice.DAL",
		"av.task01.xmlservice.BL",
		"av.task01.xmlservice.Rest",
		"av.task01.xmlservice"
})
public class XmlserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(XmlserviceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
