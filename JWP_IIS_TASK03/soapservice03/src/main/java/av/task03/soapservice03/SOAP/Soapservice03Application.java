package av.task03.soapservice03.SOAP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan(basePackages = {
		"av.task03.soapservice03",
		"av.task03.soapservice03.DAL.Entity",
		"av.task03.soapservice03.BL.Service",
		"av.task03.soapservice03.BL.ServiceImpl"
})
public class Soapservice03Application {

	public static void main(String[] args) {
		SpringApplication.run(Soapservice03Application.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
