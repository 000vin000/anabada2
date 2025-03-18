package kr.co.anabada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class AnabadaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnabadaApplication.class, args);
	}

}
