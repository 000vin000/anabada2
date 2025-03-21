package kr.co.anabada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@EnableJpaRepositories
@SpringBootApplication
public class AnabadaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnabadaApplication.class, args);
	}
}
