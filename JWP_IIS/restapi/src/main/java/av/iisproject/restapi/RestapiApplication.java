package av.iisproject.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableAsync
@EntityScan("av.iisproject.restapi.DAL.Entity")
@EnableJpaRepositories("av.iisproject.restapi.DAL.Repository")
@ComponentScan(basePackages = {
		"av.iisproject.restapi.DAL",
		"av.iisproject.restapi.BL",
		"av.iisproject.restapi",
		"av.iisproject.restapi.JWT"
		})
public class RestapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestapiApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
