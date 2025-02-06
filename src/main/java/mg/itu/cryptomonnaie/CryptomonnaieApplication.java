package mg.itu.cryptomonnaie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptomonnaieApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptomonnaieApplication.class, args);
	}
}
